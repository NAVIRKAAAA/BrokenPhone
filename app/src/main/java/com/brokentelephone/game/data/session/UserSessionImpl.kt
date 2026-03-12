package com.brokentelephone.game.data.session

import com.brokentelephone.game.domain.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class UserSessionImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) : UserSession {

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuth)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    private var firestoreListener: ListenerRegistration? = null

    override suspend fun initialize() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            try {
                val snapshot = firestore.collection(COLLECTION_USERS).document(currentUser.uid).get().await()
                val user = snapshot.data?.let { User.fromMap(it) } ?: throw SessionDataException()
                _authState.value = if (currentUser.isAnonymous) AuthState.Guest(user) else AuthState.Auth(user)
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
            }
    }

    override suspend fun updateUsername(username: String) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_USERNAME, username, User.FIELD_UPDATED_AT, System.currentTimeMillis())
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }
    override suspend fun updateAvatar(avatarUrl: String) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        try {
            firestore.collection(COLLECTION_USERS)
                .document(uid)
                .update(User.FIELD_AVATAR_URL, avatarUrl, User.FIELD_UPDATED_AT, System.currentTimeMillis())
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }
    override suspend fun completeAvatarStep(avatarUrl: String) {
        val uid = firebaseAuth.currentUser?.uid ?: return
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
        val uid = firebaseAuth.currentUser?.uid ?: return
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

    override fun getBlockedUsers(): Flow<List<BlockedUser>> = flowOf()
    override suspend fun blockUser(blockedUserId: String) = Unit
    override suspend fun unblockUser(blockId: String) = Unit
    override suspend fun updateNotifications(notifications: List<NotificationType>) = Unit
    override suspend fun signOut() = firebaseAuth.signOut()
    override suspend fun deleteAccount() = Unit

    companion object {
        private const val COLLECTION_USERS = "users"
    }
}
