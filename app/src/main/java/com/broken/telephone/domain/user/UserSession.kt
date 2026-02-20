package com.broken.telephone.domain.user

import com.broken.telephone.domain.post.Post
import kotlinx.coroutines.flow.Flow

interface UserSession {
    val authState: Flow<AuthState>
    fun getMyPosts(): Flow<List<Post>>
    fun getMyContributions(): Flow<List<Post>>
    suspend fun updateProfile(username: String)
}
