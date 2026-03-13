package com.brokentelephone.game.data.repository

import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.post.PostChainEntry
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.post.PostStatus
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository(firestore), PostRepository {

    override val collectionName = "posts"

    override fun getPosts(): Flow<List<Post>> = flowOf(emptyList())

    override fun getPostById(id: String): Flow<Post?> = flowOf(null)

    override fun getChainByPostId(postId: String): Flow<List<PostChainEntry>> = flowOf(emptyList())

    override fun getUserPosts(userId: String): Flow<List<Post>> = flowOf(emptyList())

    override fun getUserContributions(userId: String): Flow<List<Post>> = flowOf(emptyList())

    override suspend fun updatePost(post: Post) = Unit

    override suspend fun createPost(
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ) {
        val docRef = collection.document()
        val now = System.currentTimeMillis()

        val post = Post(
            id = docRef.id,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            createdAt = now,
            updatedAt = now,
            generation = 1,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
            currentEntry = PostChainEntry(
                parentId = docRef.id,
                authorId = authorId,
                authorName = authorName,
                avatarUrl = avatarUrl,
                content = PostContent.Text(text = text),
                createdAt = now,
                updatedAt = now,
                status = PostStatus.AVAILABLE,
            ),
        )
        try {
            docRef.set(post.toMap()).await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun deletePost(postId: String) = Unit
}
