package com.brokentelephone.game.domain.user

import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.NotificationType
import kotlinx.coroutines.flow.Flow

interface UserSession {
    val authState: Flow<AuthState>
    val user: Flow<User?>

    suspend fun getUserId(): String?
    suspend fun initialize()
    suspend fun reloadUser()
    suspend fun updateUsername(username: String)
    suspend fun updateBio(bio: String)
    suspend fun updateAvatar(avatarUrl: String)
    suspend fun completeAvatarStep(avatarUrl: String)
    suspend fun completeUsernameStep(username: String)
    suspend fun markPostAsNotInterested(postId: String)
    suspend fun getNotInterestedPostIds(): List<String>
    suspend fun blockUser(userId: String)
    suspend fun unblockUser(userId: String)
    suspend fun updateNotifications(notifications: List<NotificationType>)
    suspend fun updatePermissions(permissions: UserPermissions)
    suspend fun updateFcmToken(token: String)
    fun refreshFcmToken()
    suspend fun deleteFcmToken()
    suspend fun updatePassword(newPassword: String)
    suspend fun signOut()
    suspend fun deleteAccount()
    suspend fun getBlockedUsers(): List<BlockedUser>
    suspend fun getExcludedUserIds(): List<String>
}
