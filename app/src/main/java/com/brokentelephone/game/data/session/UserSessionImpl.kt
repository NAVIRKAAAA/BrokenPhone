package com.brokentelephone.game.data.session

import android.util.Log
import com.brokentelephone.game.data.ext.toAuthProvider
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserSessionImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : UserSession {

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuth)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    private var firestoreListener: ListenerRegistration? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override suspend fun initialize() {
        val currentUser = firebaseAuth.currentUser

        Log.d("LOG_TAG", "initialize: $currentUser")

        if (currentUser != null) {
            try {
                val snapshot =
                    firestore.collection(COLLECTION_USERS).document(currentUser.uid).get().await()
                val user = snapshot.data?.let { User.fromMap(it) } ?: throw SessionDataException()
                _authState.value =
                    if (currentUser.isAnonymous) AuthState.Guest(user) else AuthState.Auth(user)
            } catch (e: SessionDataException) {
                throw e
            } catch (_: Exception) {
                throw SessionDataException()
            }
        } else {
            _authState.value = AuthState.NotAuth
        }

        firebaseAuth.addAuthStateListener { auth ->
            val user = auth.currentUser

            Log.d("LOG_TAG", "isEmailVerified(): ${user?.isEmailVerified}")

            firestoreListener?.remove()
            firestoreListener = null

            if (user != null) {
                observeFirestoreUser(uid = user.uid, isAnonymous = user.isAnonymous)
            } else {
                _authState.value = AuthState.NotAuth
            }
        }
    }

    private fun observeFirestoreUser(uid: String, isAnonymous: Boolean) {
        firestoreListener = firestore
            .collection(COLLECTION_USERS)
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val user = snapshot.data?.let { User.fromMap(it) } ?: return@addSnapshotListener
                _authState.value = if (isAnonymous) AuthState.Guest(user) else AuthState.Auth(user)
                if (!isAnonymous) {
                    scope.launch { syncEmailIfNeeded(uid, user.email) }
                    scope.launch { syncAuthProviderIfNeeded(uid, user.authProvider) }
                    scope.launch { syncIsEmailVerifiedIfNeeded(uid, user.isEmailVerified) }
                }
            }
    }

    private suspend fun syncIsEmailVerifiedIfNeeded(uid: String, firestoreIsEmailVerified: Boolean) {
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
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_USERNAME,
                    username,
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
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_AVATAR_URL, avatarUrl,
                    User.FIELD_ONBOARDING_STEP, OnboardingStep.CHOOSE_USERNAME.name,
                    User.FIELD_UPDATED_AT, System.currentTimeMillis(),
                )
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeUsernameStep(username: String) {
        val uid = firebaseAuth.currentUser?.uid ?: throw UnauthorizedException()
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(
                    User.FIELD_USERNAME, username,
                    User.FIELD_ONBOARDING_STEP, OnboardingStep.COMPLETED.name,
                    User.FIELD_UPDATED_AT, System.currentTimeMillis(),
                )
                .await()
        } catch (_: FirebaseNetworkException) {
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
    override suspend fun updateNotifications(notifications: List<NotificationType>) = Unit
    override suspend fun signOut() = firebaseAuth.signOut()
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
                .also { Log.d("LOG_TAG", "getBlockedUsers: documents: ${it.size}") }
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
