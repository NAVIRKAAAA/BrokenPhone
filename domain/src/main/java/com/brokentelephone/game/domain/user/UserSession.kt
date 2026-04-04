package com.brokentelephone.game.domain.user

import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.model.settings.NotificationType
import kotlinx.coroutines.flow.Flow

interface UserSession {
    val authState: Flow<AuthState>
    suspend fun initialize()
    suspend fun updateUsername(username: String)
    suspend fun updateBio(bio: String)
    suspend fun updateAvatar(avatarUrl: String)
    suspend fun completeAvatarStep(avatarUrl: String)
    suspend fun completeUsernameStep(username: String)
    suspend fun markPostAsNotInterested(postId: String)
    suspend fun blockUser(userId: String)
    suspend fun unblockUser(userId: String)
    suspend fun updateNotifications(notifications: List<NotificationType>)
    suspend fun updateLanguage(language: Language)
    suspend fun updateFcmToken(token: String)
    suspend fun signOut()
    suspend fun deleteAccount()
    suspend fun getBlockedUsers(): List<BlockedUser>
}
