package com.brokentelephone.game.data.session

import android.util.Log
import com.brokentelephone.game.data.dto.UserDto
import com.brokentelephone.game.data.ext.toAuthProvider
import com.brokentelephone.game.data.mapper.toUser
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserSessionImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val supabase: SupabaseClient,
) : UserSession {

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuth)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    private var firestoreListener: ListenerRegistration? = null
    private var realtimeChannel: RealtimeChannel? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun initialize() {
        Log.d("LOG_TAG", "initialize()")
        val status = supabase.auth.sessionStatus
            .first { it is SessionStatus.Authenticated || it is SessionStatus.NotAuthenticated }

        Log.d("LOG_TAG", "sessionStatus: $status")

        if (status is SessionStatus.Authenticated) {
            val userId = supabase.auth.currentUserOrNull()?.id ?: throw SessionDataException()

            try {
                val rawResult = supabase.from("users")
                    .select { filter { eq("id", userId) } }
                val user = rawResult
                    .decodeSingleOrNull<UserDto>()
                    ?.toUser()
                    ?: throw SessionDataException()

                val isGuest = user.authProvider == AuthProvider.GUEST

                _authState.value =
                    if (isGuest) AuthState.Guest(user) else AuthState.Auth(user)

                observeSupabaseUser(userId)
            } catch (e: SessionDataException) {
                throw e
            } catch (_: Exception) {
                throw SessionDataException()
            }
        } else {
            _authState.value = AuthState.NotAuth
        }
    }

    private fun observeSupabaseUser(userId: String) {
        realtimeChannel?.let { channel ->
            scope.launch { supabase.realtime.removeChannel(channel) }
        }

        val channel = supabase.realtime.channel("user-$userId")
        realtimeChannel = channel

        val updateFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
            table = "users"
            filter(
                FilterOperation(
                    column = "id",
                    operator = FilterOperator.EQ,
                    value = userId
                )
            )
        }

        scope.launch {
            updateFlow.collect { change ->
                Log.d("LOG_TAG", "New user change")
                try {
                    val updatedUser = change.decodeRecord<UserDto>().toUser()
                    Log.d("LOG_TAG", "New user: $updatedUser")
                    _authState.value = when (updatedUser.authProvider) {
                        AuthProvider.GUEST -> AuthState.Guest(updatedUser)
                        else -> AuthState.Auth(updatedUser)
                    }
                } catch (_: Exception) { }
            }
        }

        scope.launch { channel.subscribe() }
    }

    private suspend fun syncIsEmailVerifiedIfNeeded(
        uid: String,
        firestoreIsEmailVerified: Boolean
    ) {
        val authIsEmailVerified = firebaseAuth.currentUser?.isEmailVerified ?: return
        if (authIsEmailVerified == firestoreIsEmailVerified) return
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_IS_EMAIL_VERIFIED, authIsEmailVerified)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            // best-effort sync, ignore failures
        }
    }

    private suspend fun syncAuthProviderIfNeeded(uid: String, firestoreAuthProvider: AuthProvider) {
        val firebaseProvider = firebaseAuth.currentUser?.providerData
            ?.lastOrNull { it.providerId != "firebase" }
            ?.providerId
            ?.toAuthProvider() ?: return

        if (firebaseProvider == firestoreAuthProvider) return
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_AUTH_PROVIDER, firebaseProvider.name)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            // best-effort sync, ignore failures
        }
    }

    private suspend fun syncEmailIfNeeded(uid: String, firestoreEmail: String) {
        val authEmail = firebaseAuth.currentUser?.email ?: return
        if (authEmail == firestoreEmail) return
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_EMAIL, authEmail)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            // best-effort sync, ignore failures
        }
    }

    override suspend fun updateUsername(username: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("users").update(
                buildJsonObject {
                    put("username", username)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateBio(bio: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_BIO,
                    bio,
                    User.FIELD_UPDATED_AT,
                    System.currentTimeMillis()
                )
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateAvatar(avatarUrl: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_AVATAR_URL,
                    avatarUrl,
                    User.FIELD_UPDATED_AT,
                    System.currentTimeMillis()
                )
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeAvatarStep(avatarUrl: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("users").update(
                buildJsonObject {
                    put("avatar_url", avatarUrl)
                    put("onboarding_step", OnboardingStep.CHOOSE_USERNAME.name)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeUsernameStep(username: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("users").update(
                buildJsonObject {
                    put("username", username)
                    put("onboarding_step", OnboardingStep.COMPLETED.name)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun markPostAsNotInterested(postId: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_NOT_INTERESTED_POST_IDS, FieldValue.arrayUnion(postId))
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun blockUser(userId: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            val userDoc = firestore.collection(COLLECTION_USERS).document(uid)
            val blockedUserDoc = userDoc.collection(COLLECTION_BLOCKED_USERS).document(userId)
            val blockedUser = BlockedUser(
                id = userId,
                userId = userId,
                createdAt = System.currentTimeMillis(),
            )
            val targetUserDoc = firestore.collection(COLLECTION_USERS).document(userId)
            firestore.runBatch { batch ->
                batch.update(userDoc, User.FIELD_BLOCKED_USER_IDS, FieldValue.arrayUnion(userId))
                batch.update(targetUserDoc, User.FIELD_BLOCKED_BY, FieldValue.arrayUnion(uid))
                batch.set(blockedUserDoc, blockedUser.toMap())
            }.await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun unblockUser(userId: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            val userDoc = firestore.collection(COLLECTION_USERS).document(uid)
            val blockedUserDoc = userDoc.collection(COLLECTION_BLOCKED_USERS).document(userId)
            val targetUserDoc = firestore.collection(COLLECTION_USERS).document(userId)
            firestore.runBatch { batch ->
                batch.update(userDoc, User.FIELD_BLOCKED_USER_IDS, FieldValue.arrayRemove(userId))
                batch.update(targetUserDoc, User.FIELD_BLOCKED_BY, FieldValue.arrayRemove(uid))
                batch.delete(blockedUserDoc)
            }.await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateNotifications(notifications: List<NotificationType>) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_NOTIFICATIONS,
                    notifications.map { it.name },
                    User.FIELD_UPDATED_AT,
                    System.currentTimeMillis()
                )
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updatePermissions(permissions: UserPermissions) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()

        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_PERMISSIONS,
                    permissions.toMap(),
                    User.FIELD_UPDATED_AT,
                    System.currentTimeMillis()
                )
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateLanguage(language: Language) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_LANGUAGE,
                    language.name,
                    User.FIELD_UPDATED_AT,
                    System.currentTimeMillis()
                )
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun deleteFcmToken() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_FCM_TOKEN, null)
                .await()
            FirebaseMessaging.getInstance().deleteToken().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun refreshFcmToken() {
        try {
            val token = FirebaseMessaging.getInstance().token.await()
            Log.d("LOG_TAG", "refreshFcmToken: $token")
            updateFcmToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateFcmToken(token: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_FCM_TOKEN, token)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
            // best-effort, ignore failures
        }
    }

    override suspend fun signOut() {
        realtimeChannel?.let { supabase.realtime.removeChannel(it) }
        realtimeChannel = null
        supabase.auth.signOut()
        _authState.value = AuthState.NotAuth
    }

    override suspend fun deleteAccount() = Unit

    override suspend fun getBlockedUsers(): List<BlockedUser> {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        return try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .collection(COLLECTION_BLOCKED_USERS)
                .get()
                .await()
                .documents
                .mapNotNull { it.data?.let(BlockedUser::fromMap) }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_BLOCKED_USERS = "blockedUsers"
    }
}
