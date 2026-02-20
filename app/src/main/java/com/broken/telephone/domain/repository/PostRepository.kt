package com.broken.telephone.domain.repository

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostChainEntry
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPosts(): Flow<List<Post>>

    fun getPostById(id: String): Flow<Post?>

    fun getChainByPostId(postId: String): Flow<List<PostChainEntry>>

    suspend fun updatePost(post: Post)

    suspend fun createPost(post: Post)

}
