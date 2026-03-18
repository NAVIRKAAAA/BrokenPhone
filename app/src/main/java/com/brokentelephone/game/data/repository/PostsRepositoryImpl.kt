package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.data.mapper.toPost
import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.essentials.exceptions.auth.CannotDeletePostException
import com.brokentelephone.game.essentials.exceptions.auth.ImageUploadException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.PostInProgressException
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl(
    firestore: FirebaseFirestore,
    private val imageStorage: ImageStorage,
) : FirestoreRepository(firestore), PostRepository {

    override val collectionName = "posts"

    override suspend fun loadInitialPosts(
        pageSize: Int,
        sort: DashboardSort,
        userId: String,
        blockedUsersIds: List<String>,
        blockedBy: List<String>,
        notInterestedPostIds: List<String>
    ): PostsPage {
        try {
            val snapshot = collection
                .applySorting(sort)
                .limit(pageSize.toLong())
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { it.data?.toPost() }

            val hasMore = posts.size >= pageSize
            val excludedAuthorIds = (blockedUsersIds + blockedBy + userId).toSet()
            val filteredPosts = posts.filter {
                it.authorId !in excludedAuthorIds &&
                        it.id !in notInterestedPostIds &&
                        it.parentId !in notInterestedPostIds
            }

            val lastDocRef = snapshot.documents.lastOrNull()
            return PostsPage(posts = filteredPosts, lastDocRef = lastDocRef, hasMore = hasMore)
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun loadNextPosts(
        afterDoc: DocumentSnapshot,
        pageSize: Int,
        sort: DashboardSort,
        userId: String,
        blockedUsersIds: List<String>,
        blockedBy: List<String>,
        notInterestedPostIds: List<String>
    ): PostsPage {
        try {
            val excludedAuthorIds = (blockedUsersIds + blockedBy + userId).toSet()
            val snapshot = collection
                .applySorting(sort)
                .startAfter(afterDoc)
                .limit(pageSize.toLong())
                .get()
                .await()

            val posts = snapshot.documents.mapNotNull { it.data?.toPost() }
            val filteredPosts = posts.filter {
                it.authorId !in excludedAuthorIds &&
                        it.id !in notInterestedPostIds &&
                        it.parentId !in notInterestedPostIds
            }

            val hasMore = posts.size >= pageSize

            val lastDocRef = snapshot.documents.lastOrNull()
            return PostsPage(posts = filteredPosts, lastDocRef = lastDocRef, hasMore = hasMore)
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
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

    override suspend fun getChainByPostId(postId: String): List<Post> {
        return try {
            val snapshot = collection.document(postId)
                .collection(CHAIN_COLLECTION)
                .orderBy(FIELD_CREATED_AT, Query.Direction.ASCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.data?.toPost() }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

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
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
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
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun submitContinuation(
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
    ) {
        val uploadedContent = when {
            content is PostContent.Drawing && content.localPath != null -> {
                val localPath = content.localPath ?: throw ImageUploadException()
                val imageUrl = try {
                    imageStorage.uploadImage(localPath)
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
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
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
            status = PostStatus.AVAILABLE
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
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
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
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun deletePost(postId: String, parentId: String) {
        try {
            if (postId == parentId) {
                val postDoc = collection.document(postId).get().await()
                deleteRootPost(postId, postDoc)
            } else {
                deleteContinuation(postId, rootPostId = parentId)
            }
        } catch (e: AppException) {
            throw e
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    private suspend fun deleteRootPost(postId: String, postDoc: DocumentSnapshot) {
        val post = postDoc.data?.toPost() ?: throw PostNotFoundException()

        if (post.status == PostStatus.IN_PROGRESS) throw PostInProgressException()

        val chainSnapshot = collection.document(postId).collection(CHAIN_COLLECTION).get().await()
        val chainCount = chainSnapshot.size()
        val canDelete = chainCount == 1 || post.generation != post.maxGenerations
        if (!canDelete) throw CannotDeletePostException()

        val userPostRef = collection.firestore
            .collection(USERS_COLLECTION)
            .document(post.authorId)
            .collection(USER_POSTS_COLLECTION)
            .document(postId)

        collection.firestore.runBatch { batch ->
            batch.delete(collection.document(postId))
            batch.delete(userPostRef)
            chainSnapshot.documents.forEach { batch.delete(it.reference) }
        }.await()
    }

    private suspend fun deleteContinuation(postId: String, rootPostId: String) {

        val chainEntryDoc = collection.document(rootPostId)
            .collection(CHAIN_COLLECTION)
            .whereEqualTo(FIELD_ID, postId)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull() ?: throw PostNotFoundException()

        val chainEntry = chainEntryDoc.data?.toPost() ?: throw PostNotFoundException()

        val rootPost = collection.document(rootPostId).get().await()
            .data?.toPost() ?: throw PostNotFoundException()

        if (rootPost.status == PostStatus.IN_PROGRESS) {
            throw PostInProgressException()
        }
        if (chainEntry.generation != rootPost.generation) {
            throw CannotDeletePostException()
        }
        if (chainEntry.generation == rootPost.maxGenerations) {
            throw CannotDeletePostException()
        }

        val prevEntry = collection.document(rootPostId)
            .collection(CHAIN_COLLECTION)
            .whereEqualTo(FIELD_GENERATION, chainEntry.generation - 1)
            .limit(1)
            .get()
            .await()
            .documents
            .firstOrNull()?.data?.toPost() ?: throw PostNotFoundException()

        val rolledBackPost = rootPost.copy(
            authorId = prevEntry.authorId,
            authorName = prevEntry.authorName,
            avatarUrl = prevEntry.avatarUrl,
            content = prevEntry.content,
            generation = prevEntry.generation,
            updatedAt = prevEntry.updatedAt,
            status = PostStatus.AVAILABLE,
        )

        val contributionRef = collection.firestore
            .collection(USERS_COLLECTION)
            .document(chainEntry.authorId)
            .collection(CONTRIBUTIONS_COLLECTION)
            .document(postId)

        collection.firestore.runBatch { batch ->
            batch.set(collection.document(rootPostId), rolledBackPost.toMap())
            batch.delete(chainEntryDoc.reference)
            batch.delete(contributionRef)
        }.await()
    }

    private fun contributionsCollection(userId: String) = collection.firestore
        .collection(USERS_COLLECTION)
        .document(userId)
        .collection(CONTRIBUTIONS_COLLECTION)

    private fun Query.applySorting(sort: DashboardSort): Query {
        return when (sort) {
            DashboardSort.LATEST -> orderBy(
                FIELD_UPDATED_AT,
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
        const val FIELD_ID = "id"
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
