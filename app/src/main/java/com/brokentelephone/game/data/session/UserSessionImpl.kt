package com.brokentelephone.game.data.session

import com.brokentelephone.game.domain.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionImpl(
    private val firebaseAuth: FirebaseAuth,
) : UserSession {

    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuth)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override suspend fun initialize() {
        val firebaseUser = firebaseAuth.currentUser
        _authState.value = when {
            firebaseUser == null -> AuthState.NotAuth
            firebaseUser.isAnonymous -> AuthState.Guest(firebaseUser.toDomain())
            else -> AuthState.Auth(firebaseUser.toDomain())
        }

        firebaseAuth.addAuthStateListener { auth ->
            val user = auth.currentUser
            _authState.value = when {
                user == null -> AuthState.NotAuth
                user.isAnonymous -> AuthState.Guest(user.toDomain())
                else -> AuthState.Auth(user.toDomain())
            }
        }
    }

    override suspend fun updateProfile(username: String) = Unit
    override suspend fun updateAvatar(avatarUrl: String) = Unit
    override fun getBlockedUsers(): Flow<List<BlockedUser>> = kotlinx.coroutines.flow.flowOf()
    override suspend fun blockUser(blockedUserId: String) = Unit
    override suspend fun unblockUser(blockId: String) = Unit
    override suspend fun updateNotifications(enabledNotifications: List<NotificationType>) = Unit
    override suspend fun signOut() = firebaseAuth.signOut()
    override suspend fun deleteAccount() = Unit

    private fun FirebaseUser.toDomain(): User = User(
        id = uid,
        username = "",
        email = "",
        avatarUrl = "",
        createdAt = 0L,
        updatedAt = 0L,
        authProvider = AuthProvider.EMAIL,
    )
}
