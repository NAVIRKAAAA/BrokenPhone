package com.brokentelephone.game.domain.user

import com.brokentelephone.game.domain.settings.NotificationType
import kotlinx.coroutines.flow.Flow

interface UserSession {
    val authState: Flow<AuthState>
    suspend fun initialize()
    suspend fun updateProfile(username: String)
    suspend fun updateAvatar(avatarUrl: String)
    suspend fun completeAvatarStep(avatarUrl: String)
    suspend fun completeUsernameStep(username: String)
    fun getBlockedUsers(): Flow<List<BlockedUser>>
    suspend fun blockUser(blockedUserId: String)
    suspend fun unblockUser(blockId: String)
    suspend fun updateNotifications(notifications: List<NotificationType>)
    suspend fun signOut()
    suspend fun deleteAccount()
}
