package com.brokentelephone.game.data.session

import com.brokentelephone.game.domain.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.UserSession
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UserSessionImpl(
    private val firebaseAuth: FirebaseAuth,
) : UserSession {

    override val authState: Flow<AuthState>
        get() = flowOf(AuthState.NotAuth)

    override suspend fun updateProfile(username: String) {
        return
    }

    override suspend fun updateAvatar(avatarUrl: String) {
        return
    }

    override fun getBlockedUsers(): Flow<List<BlockedUser>> {
        return flowOf()
    }

    override suspend fun blockUser(blockedUserId: String) {
        return
    }

    override suspend fun unblockUser(blockId: String) {
        return
    }

    override suspend fun updateNotifications(enabledNotifications: List<NotificationType>) {
        return
    }

    override suspend fun logout() {
        return
    }

    override suspend fun deleteAccount() {
        return
    }
}