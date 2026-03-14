package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.data.mapper.toPost
import com.brokentelephone.game.data.model.PostsPage
import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.post.PostChainEntry
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.post.PostStatus
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.essentials.exceptions.auth.ImageUploadException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.features.dashboard.model.DashboardSort
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl(
    firestore: FirebaseFirestore,
    private val imageStorage: ImageStorage,
) : FirestoreRepository(firestore), PostRepository {

    override val collectionName = "posts"

    override suspend fun loadInitialPosts(
        pageSize: Int,
        sort: DashboardSort,
    ): PostsPage {
        try {
            val snapshot = collection
                .whereIn(FIELD_STATUS, ACTIVE_STATUSES)
                .applySorting(sort)
                .limit(pageSize.toLong())
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { it.data?.toPost() }
            val lastDocRef = snapshot.documents.lastOrNull()

            return PostsPage(posts = posts, lastDocRef = lastDocRef)
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun loadNextPosts(
        afterDoc: DocumentSnapshot,
        pageSize: Int,
        sort: DashboardSort,
    ): PostsPage {
        try {
            val snapshot = collection
                .applySorting(sort)
                .startAfter(afterDoc)
                .limit(pageSize.toLong())
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { it.data?.toPost() }
            val lastDocRef = snapshot.documents.lastOrNull()

            return PostsPage(posts = posts, lastDocRef = lastDocRef)
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override fun getPostById(id: String): Flow<Post> = callbackFlow {
        val listener = collection.document(id)
            .addSnapshotListener { snapshot, error ->
                val post = snapshot?.data?.toPost()
                if (post == null || error != null) {
                    close(PostNotFoundException())
                    return@addSnapshotListener
                }
                trySend(post)
            }
        awaitClose { listener.remove() }
    }

    override fun getChainByPostId(postId: String): Flow<List<PostChainEntry>> = flowOf(emptyList())

    override suspend fun loadUserPosts(userId: String): List<Post> {
        try {
            val snapshot = collection.firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(USER_POSTS_COLLECTION)
                .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()
            return snapshot.documents.mapNotNull { it.data?.toPost() }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun loadContributions(userId: String): List<Post> {
        try {
            val snapshot = contributionsCollection(userId)
                .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()
            return snapshot.documents.mapNotNull { it.data?.toPost() }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updatePost(post: Post) = Unit

    override suspend fun submitContinuation(
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
    ) {
        val uploadedContent = when {
            content is PostContent.Drawing && content.localPath != null -> {
                val imageUrl = try {
                    imageStorage.uploadImage(content.localPath)
                } catch (_: Exception) {
                    throw ImageUploadException()
                }
                PostContent.Drawing(imageUrl = imageUrl)
            }

            else -> content
        }

        val postSnapshot = try {
            collection.document(postId).get().await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }

        val post = postSnapshot.data?.toPost() ?: throw PostNotFoundException()

        val now = System.currentTimeMillis()
        val postRef = collection.document(postId)
        val childPostRef = postRef.collection(CHAIN_COLLECTION).document()
        val childPost = Post(
            id = childPostRef.id,
            parentId = postId,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = uploadedContent,
            createdAt = now,
            updatedAt = now,
            status = PostStatus.AVAILABLE,
            generation = post.generation + 1,
            maxGenerations = post.maxGenerations,
            textTimeLimit = post.textTimeLimit,
            drawingTimeLimit = post.drawingTimeLimit,
        )
        val updatedPost = post.copy(
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = uploadedContent,
            generation = post.generation + 1,
            updatedAt = now,
        )
        val contributionRef = collection.firestore
            .collection(USERS_COLLECTION)
            .document(authorId)
            .collection(CONTRIBUTIONS_COLLECTION)
            .document(childPost.id)

        try {
            collection.firestore.runBatch { batch ->
                batch.set(postRef, updatedPost.toMap())
                batch.set(childPostRef, childPost.toMap())
                batch.set(contributionRef, childPost.toMap())
            }.await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

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
            parentId = null,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = PostContent.Text(text = text),
            createdAt = now,
            updatedAt = now,
            status = PostStatus.AVAILABLE,
            generation = 1,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
        )
        val chainEntryRef = docRef.collection(CHAIN_COLLECTION).document()
        val chainEntry = post.copy(id = chainEntryRef.id, parentId = docRef.id)
        val userPostRef = collection.firestore
            .collection(USERS_COLLECTION)
            .document(authorId)
            .collection(USER_POSTS_COLLECTION)
            .document(docRef.id)

        try {
            collection.firestore.runBatch { batch ->
                batch.set(docRef, post.toMap())
                batch.set(chainEntryRef, chainEntry.toMap())
                batch.set(userPostRef, post.toMap())
            }.await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun deletePost(postId: String) = Unit

    private fun contributionsCollection(userId: String) = collection.firestore
        .collection(USERS_COLLECTION)
        .document(userId)
        .collection(CONTRIBUTIONS_COLLECTION)

    private fun Query.applySorting(sort: DashboardSort): Query {
        return when (sort) {
            DashboardSort.LATEST -> orderBy(
                FIELD_CREATED_AT,
                Query.Direction.DESCENDING
            )

            DashboardSort.ALMOST_DONE -> orderBy(FIELD_GENERATION, Query.Direction.DESCENDING)
            DashboardSort.LONGEST_CHAIN -> orderBy(
                FIELD_MAX_GENERATIONS,
                Query.Direction.DESCENDING
            )
        }
    }

    private companion object {
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_GENERATION = "generation"
        const val FIELD_MAX_GENERATIONS = "maxGenerations"
        const val FIELD_STATUS = "status"
        const val CHAIN_COLLECTION = "chain"
        const val USERS_COLLECTION = "users"
        const val CONTRIBUTIONS_COLLECTION = "contributions"
        const val USER_POSTS_COLLECTION = "posts"
        val ACTIVE_STATUSES = listOf(PostStatus.AVAILABLE.name, PostStatus.IN_PROGRESS.name)
    }
}
