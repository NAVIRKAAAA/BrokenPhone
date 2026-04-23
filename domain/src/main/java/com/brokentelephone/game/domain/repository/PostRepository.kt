package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.sort.DashboardSort
import kotlinx.coroutines.flow.Flow


interface PostRepository {

    suspend fun loadInitialPosts(
        pageSize: Int,
        sort: DashboardSort,
        excludedUserIds: List<String> = emptyList(),
        excludedPostIds: List<String> = emptyList(),
    ): PostsPage

    suspend fun loadNextPosts(
        offset: Int,
        pageSize: Int,
        sort: DashboardSort,
        excludedUserIds: List<String> = emptyList(),
        excludedPostIds: List<String> = emptyList(),
    ): PostsPage

    fun getPostById(id: String): Flow<Post>

    suspend fun getChainByPostId(postId: String): List<Post>

    suspend fun getChainById(chainId: String): List<Post>

    suspend fun loadUserPosts(userId: String): List<Post>

    suspend fun loadContributions(userId: String): List<Post>

    suspend fun createPost(
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    )

    suspend fun deletePost(postId: String)

}
