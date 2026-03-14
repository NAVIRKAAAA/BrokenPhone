package com.brokentelephone.game.data.repository

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
import com.google.firebase.firestore.CollectionReference
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

    override suspend fun loadInitialPosts(pageSize: Int, sort: DashboardSort): PostsPage {
        try {
            val snapshot = collection
                .applySorting(sort)
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

    override suspend fun loadNextPosts(afterDoc: DocumentSnapshot, pageSize: Int, sort: DashboardSort): PostsPage {
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

    override fun getPostById(id: String): Flow<Post?> = callbackFlow {
        val listener = collection.document(id)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot?.data?.toPost())
            }
        awaitClose { listener.remove() }
    }

    override fun getChainByPostId(postId: String): Flow<List<PostChainEntry>> = flowOf(emptyList())

    override fun getUserPosts(userId: String): Flow<List<Post>> = flowOf(emptyList())

    override fun getUserContributions(userId: String): Flow<List<Post>> = flowOf(emptyList())

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
        val newEntry = PostChainEntry(
            parentId = postId,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = uploadedContent,
            createdAt = now,
            updatedAt = now,
            status = PostStatus.AVAILABLE,
        )
        val updatedPost = post.copy(
            generation = post.generation + 1,
            updatedAt = now,
            currentEntry = newEntry,
        )

        val postRef = collection.document(postId)
        val chainEntryRef = postRef.collection(CHAIN_COLLECTION).document(newEntry.id)
        val contributionRef = collection.firestore
            .collection(USERS_COLLECTION)
            .document(authorId)
            .collection(CONTRIBUTIONS_COLLECTION)
            .document(newEntry.id)

        try {
            collection.firestore.runBatch { batch ->
                batch.set(postRef, updatedPost.toMap())
                batch.set(chainEntryRef, newEntry.toMap())
                batch.set(contributionRef, newEntry.toMap())
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
        val chainEntryRef = collection
            .document(docRef.id)
            .collection(CHAIN_COLLECTION)
            .document(post.currentEntry.id)

        try {
            collection.firestore.runBatch { batch ->
                batch.set(docRef, post.toMap())
                batch.set(chainEntryRef, post.currentEntry.toMap())
            }.await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun deletePost(postId: String) = Unit

    private fun CollectionReference.applySorting(sort: DashboardSort): Query {
        return when (sort) {
            DashboardSort.JUST_STARTED -> orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
            DashboardSort.ALMOST_DONE -> orderBy(FIELD_GENERATION, Query.Direction.DESCENDING)
            DashboardSort.LONGEST_CHAIN -> orderBy(FIELD_MAX_GENERATIONS, Query.Direction.DESCENDING)
        }
    }

    private companion object {
        const val FIELD_GENERATION = "generation"
        const val CHAIN_COLLECTION = "chain"
        const val USERS_COLLECTION = "users"
        const val CONTRIBUTIONS_COLLECTION = "contributions"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_MAX_GENERATIONS = "maxGenerations"
    }
}
