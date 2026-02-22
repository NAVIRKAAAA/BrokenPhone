package com.broken.telephone.domain.user

import com.broken.telephone.domain.post.Post
import kotlinx.coroutines.flow.Flow

interface UserSession {
    val authState: Flow<AuthState>
    fun getMyPosts(): Flow<List<Post>>
    fun getMyContributions(): Flow<List<Post>>
    suspend fun updateProfile(username: String)
    suspend fun updateAvatar(avatarUrl: String)
    fun getBlockedUsers(): Flow<List<BlockedUser>>
    suspend fun blockUser(blockedUserId: String)
    suspend fun unblockUser(blockId: String)
    suspend fun logout()
    suspend fun deleteAccount()
}
