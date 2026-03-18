package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow


interface PostRepository {

    suspend fun loadInitialPosts(
        pageSize: Int,
        sort: DashboardSort,
        userId: String,
        blockedUsersIds: List<String>,
        blockedBy: List<String>,
        notInterestedPostIds: List<String>
    ): PostsPage

    suspend fun loadNextPosts(
        afterDoc: DocumentSnapshot,
        pageSize: Int,
        sort: DashboardSort,
        userId: String,
        blockedUsersIds: List<String>,
        blockedBy: List<String>,
        notInterestedPostIds: List<String>
    ): PostsPage

    fun getPostById(id: String): Flow<Post>

    suspend fun getChainByPostId(postId: String): List<Post>

    suspend fun loadUserPosts(userId: String): List<Post>

    suspend fun loadContributions(userId: String): List<Post>

    suspend fun submitContinuation(
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
    )

    suspend fun createPost(
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    )

    suspend fun deletePost(postId: String, parentId: String)


}
