package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.post.PostChainEntry
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPosts(): Flow<List<Post>>

    fun getPostById(id: String): Flow<Post?>

    fun getChainByPostId(postId: String): Flow<List<PostChainEntry>>

    fun getUserPosts(userId: String): Flow<List<Post>>

    fun getUserContributions(userId: String): Flow<List<Post>>

    suspend fun updatePost(post: Post)

    suspend fun createPost(post: Post)

    suspend fun deletePost(postId: String)


}
