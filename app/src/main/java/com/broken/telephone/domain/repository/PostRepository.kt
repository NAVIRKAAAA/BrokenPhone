package com.broken.telephone.domain.repository

import com.broken.telephone.domain.post.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    fun getPosts(): Flow<List<Post>>

    fun getPostById(id: String): Flow<Post?>

    suspend fun updatePost(post: Post)

}
