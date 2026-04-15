package com.brokentelephone.game.data.session

import android.util.Log
import com.brokentelephone.game.data.dto.UserBlockDto
import com.brokentelephone.game.data.dto.UserDto
import com.brokentelephone.game.data.dto.toBlockedUser
import com.brokentelephone.game.data.mapper.toUser
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
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
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserSessionImpl(
    private val supabase: SupabaseClient,
) : UserSession {

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuth)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    private var realtimeChannel: RealtimeChannel? = null
    private var realtimeCollectJob: kotlinx.coroutines.Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun initialize() {
        Log.d("LOG_TAG", "initialize()")

        val status = supabase.auth.sessionStatus
            .first { it is SessionStatus.Authenticated || it is SessionStatus.NotAuthenticated }

        Log.d("LOG_TAG", "sessionStatus: $status")

        if (status is SessionStatus.Authenticated) {
            val userId = supabase.auth.currentUserOrNull()?.id ?: throw SessionDataException()

            try {
                val rawResult = supabase.from(COLLECTION_USERS)
                    .select { filter { eq("id", userId) } }
                val user = rawResult
                    .decodeSingleOrNull<UserDto>()
                    ?.toUser()
                    ?: throw SessionDataException()

                _authState.value = AuthState.Auth(user)
            } catch (e: SessionDataException) {
                throw e
            } catch (_: Exception) {
                throw SessionDataException()
            }
        } else {
            _authState.value = AuthState.NotAuth
        }

        scope.launch {
            supabase.auth.sessionStatus.collect { newSessionStatus ->
                Log.d("LOG_TAG", "newSessionStatus: $newSessionStatus")
                val currentUserId =
                    supabase.auth.currentUserOrNull()?.id ?: return@collect

                observeSupabaseUser(currentUserId)
            }
        }
    }

    private fun observeSupabaseUser(userId: String) {
        realtimeCollectJob?.cancel()
        realtimeChannel?.let { channel ->
            scope.launch { supabase.realtime.removeChannel(channel) }
        }

        val channel = supabase.realtime.channel("user-$userId")
        realtimeChannel = channel

        val updateFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
            table = COLLECTION_USERS
            filter(
                FilterOperation(
                    column = "id",
                    operator = FilterOperator.EQ,
                    value = userId
                )
            )
        }

        realtimeCollectJob = scope.launch {
            try {
                val user = supabase.from(COLLECTION_USERS)
                    .select { filter { eq("id", userId) } }
                    .decodeSingleOrNull<UserDto>()
                    ?.toUser()
                if (user != null) _authState.value = AuthState.Auth(user)
            } catch (_: Exception) { }

            updateFlow.collect { change ->
                try {
                    val updatedUser = change.decodeRecord<UserDto>().toUser()
                    _authState.value = AuthState.Auth(updatedUser)
                } catch (_: Exception) { }
            }
        }

        scope.launch { channel.subscribe() }
    }

    override suspend fun updateUsername(username: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
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
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("bio", bio)
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

    override suspend fun updateAvatar(avatarUrl: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("avatar_url", avatarUrl)
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

    override suspend fun completeAvatarStep(avatarUrl: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
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
            supabase.from(COLLECTION_USERS).update(
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
        return
    }

    override suspend fun blockUser(userId: String) {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("user_blocks").insert(
                buildJsonObject {
                    put("blocker_id", currentUserId)
                    put("blocked_id", userId)
                    put("created_at", System.currentTimeMillis())
                }
            )
        } catch (e: RestException) {
            Log.d("LOG_TAG", "blockUser(): $e")
            throw UnknownAuthException()
        } catch (e: java.io.IOException) {
            Log.d("LOG_TAG", "blockUser(): $e")
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "blockUser(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun unblockUser(userId: String) {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("user_blocks").delete {
                filter {
                    eq("blocker_id", currentUserId)
                    eq("blocked_id", userId)
                }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateNotifications(notifications: List<NotificationType>) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put(
                        "notifications",
                        buildJsonArray { notifications.forEach { add(JsonPrimitive(it.name)) } })
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

    override suspend fun updatePermissions(permissions: UserPermissions) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("permissions", buildJsonObject {
                        put("isNotificationsGranted", permissions.isNotificationsGranted)
                    })
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

    override suspend fun deleteFcmToken() {
        return
//        val uid = firebaseAuth.currentUser?.uid ?: return
//        try {
//            firestore.collection(COLLECTION_USERS)
//                .document(uid)
//                .update(User.FIELD_FCM_TOKEN, null)
//                .await()
//            FirebaseMessaging.getInstance().deleteToken().await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    override suspend fun refreshFcmToken() {
        return
//        try {
//            val token = FirebaseMessaging.getInstance().token.await()
//            Log.d("LOG_TAG", "refreshFcmToken: $token")
//            updateFcmToken(token)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    override suspend fun updateFcmToken(token: String) {
        return
//        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
//        try {
//            firestore.collection(COLLECTION_USERS)
//                .document(uid)
//                .update(User.FIELD_FCM_TOKEN, token)
//                .await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // best-effort, ignore failures
//        }
    }

    override suspend fun signOut() {
        realtimeChannel?.let { supabase.realtime.removeChannel(it) }
        realtimeChannel = null
        supabase.auth.signOut()
        _authState.value = AuthState.NotAuth
    }

    override suspend fun deleteAccount() = Unit

    override suspend fun getBlockedUsers(): List<BlockedUser> {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        return try {
            supabase.from(COLLECTION_USER_BLOCKS)
                .select { filter { eq("blocker_id", currentUserId) } }
                .decodeList<UserBlockDto>()
                .map { it.toBlockedUser() }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_USER_BLOCKS = "user_blocks"
    }
}
