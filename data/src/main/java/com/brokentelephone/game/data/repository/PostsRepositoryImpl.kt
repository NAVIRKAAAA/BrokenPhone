package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.data.mapper.toPost
import com.brokentelephone.game.domain.model.chain.Chain
import com.brokentelephone.game.domain.model.chain.ChainStatus
import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.essentials.exceptions.auth.CannotDeletePostException
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
import com.google.firebase.firestore.Source
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostsRepositoryImpl(
    firestore: FirebaseFirestore,
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
                .get(Source.SERVER)
                .await()

            val posts = snapshot.documents.mapNotNull { it.data?.toPost() }

            val hasMore = posts.size >= pageSize
            val excludedAuthorIds = (blockedUsersIds + blockedBy + userId).toSet()
            val filteredPosts = posts
                .filter { !(it.status == PostStatus.COMPLETED && it.generation < it.maxGenerations) }
//                .filter {
//                it.authorId !in excludedAuthorIds &&
//                        it.id !in notInterestedPostIds
//            }

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
                .get(Source.SERVER)
                .await()

            val posts = snapshot.documents.mapNotNull { it.data?.toPost() }
            val filteredPosts = posts
                .filter { !(it.status == PostStatus.COMPLETED && it.generation < it.maxGenerations) }
//                .filter {
//                it.authorId !in excludedAuthorIds &&
//                        it.id !in notInterestedPostIds
//            }

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
            val post = collection.document(postId).get().await()
                .data?.toPost() ?: throw PostNotFoundException()

            val snapshot = collection
                .whereEqualTo(FIELD_CHAIN_ID, post.chainId)
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
        val chainRef = collection.firestore.collection(CHAINS_COLLECTION).document()
        val now = System.currentTimeMillis()

        val post = Post(
            id = docRef.id,
            chainId = chainRef.id,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = PostContent.Text(text = text),
            createdAt = now,
            updatedAt = now,
            status = PostStatus.AVAILABLE,
            sessionId = null,
            generation = 0,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
        )
        val chain = Chain(
            id = chainRef.id,
            createdAt = now,
            status = ChainStatus.ACTIVE,
            generation = 0,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
        )
        val userPostRef = collection.firestore
            .collection(USERS_COLLECTION)
            .document(authorId)
            .collection(USER_POSTS_COLLECTION)
            .document(docRef.id)

        try {
            collection.firestore.runBatch { batch ->
                batch.set(docRef, post.toMap())
                batch.set(chainRef, chain.toMap())
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

    override suspend fun deletePost(postId: String) {
        try {
            val post = collection.document(postId).get().await()
                .data?.toPost() ?: throw PostNotFoundException()

            val chainGeneration = collection.firestore
                .collection(CHAINS_COLLECTION)
                .document(post.chainId)
                .get()
                .await()
                .getLong(FIELD_GENERATION)?.toInt() ?: throw PostNotFoundException()

            if (post.status == PostStatus.IN_PROGRESS) throw PostInProgressException()
            if (chainGeneration != post.generation) throw CannotDeletePostException()
            if (chainGeneration == post.maxGenerations) throw CannotDeletePostException()

            val postRef = collection.document(postId)
            val chainRef = collection.firestore
                .collection(CHAINS_COLLECTION)
                .document(post.chainId)
            val userPostRef = collection.firestore
                .collection(USERS_COLLECTION)
                .document(post.authorId)
                .collection(USER_POSTS_COLLECTION)
                .document(postId)
            val userContributionRef = collection.firestore
                .collection(USERS_COLLECTION)
                .document(post.authorId)
                .collection(CONTRIBUTIONS_COLLECTION)
                .document(postId)

            val userDocRef = if (userPostRef.get().await().exists()) {
                userPostRef
            } else {
                userContributionRef
            }

            if (post.generation == 1) {
                collection.firestore.runBatch { batch ->
                    batch.delete(postRef)
                    batch.delete(chainRef)
                    batch.delete(userDocRef)
                }.await()
            } else {
                val prevPostDoc = collection
                    .whereEqualTo(FIELD_CHAIN_ID, post.chainId)
                    .whereEqualTo(FIELD_GENERATION, post.generation - 1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull() ?: throw PostNotFoundException()

                collection.firestore.runBatch { batch ->
                    batch.delete(postRef)
                    batch.update(chainRef, FIELD_GENERATION, post.generation - 1)
                    batch.update(prevPostDoc.reference, FIELD_STATUS, PostStatus.AVAILABLE.name)
                    batch.delete(userDocRef)
                }.await()
            }
        } catch (e: AppException) {
            throw e
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "deletePost: $e")
            throw UnknownAuthException()
        }
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
        const val FIELD_CHAIN_ID = "chainId"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
        const val FIELD_GENERATION = "generation"
        const val FIELD_MAX_GENERATIONS = "maxGenerations"
        const val FIELD_STATUS = "status"
        const val CHAINS_COLLECTION = "chains"
        const val CHAIN_POSTS_COLLECTION = "posts"
        const val USERS_COLLECTION = "users"
        const val CONTRIBUTIONS_COLLECTION = "contributions"
        const val USER_POSTS_COLLECTION = "posts"
        val ACTIVE_STATUSES = listOf(PostStatus.AVAILABLE.name, PostStatus.IN_PROGRESS.name)
    }
}
