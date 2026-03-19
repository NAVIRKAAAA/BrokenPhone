package com.brokentelephone.game.data.repository

import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toGameSession
import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.data.mapper.toPost
import com.brokentelephone.game.domain.model.chain.ChainStatus
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.session.GameSessionStatus
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.PostUnavailableToJoinException
import com.brokentelephone.game.essentials.exceptions.auth.SessionExpiredException
import com.brokentelephone.game.essentials.exceptions.auth.SessionNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.SessionValidationException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class GameSessionRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository(firestore), GameSessionRepository {

    override val collectionName = "sessions"

    override suspend fun joinSession(postId: String, userId: String, timeLimit: Int): GameSession {
        try {
            val postRef = collection.firestore
                .collection(POSTS_COLLECTION)
                .document(postId)
            val sessionRef = collection.document()
            val userRef = collection.firestore
                .collection(USERS_COLLECTION)
                .document(userId)

            val session = collection.firestore.runTransaction { transaction ->
                val post = transaction.get(postRef).data?.toPost() ?: throw PostNotFoundException()

                if (post.status != PostStatus.AVAILABLE) throw PostUnavailableToJoinException()

                val now = System.currentTimeMillis()
                val session = GameSession(
                    id = sessionRef.id,
                    userId = userId,
                    postId = postId,
                    lockedAt = now,
                    expiresAt = now + timeLimit * 1000L + NETWORK_BUFFER_MS,
                    status = GameSessionStatus.ACTIVE,
                )

                transaction.set(sessionRef, session.toMap())
                transaction.update(
                    postRef, mapOf(
                        FIELD_STATUS to PostStatus.IN_PROGRESS.name,
                        FIELD_SESSION_ID to sessionRef.id,
                    )
                )
                transaction.update(userRef, FIELD_SESSION_ID, sessionRef.id)

                session
            }.await()

            return session
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeSession(
        sessionId: String,
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
    ) {
        try {
            val postRef = collection.firestore.collection(POSTS_COLLECTION).document(postId)
            val sessionRef = collection.document(sessionId)
            val newPostRef = collection.firestore.collection(POSTS_COLLECTION).document()
            val userRef = collection.firestore.collection(USERS_COLLECTION).document(authorId)

            collection.firestore.runTransaction { transaction ->
                val session = transaction.get(sessionRef).data?.toGameSession()
                    ?: throw SessionNotFoundException()

                if (session.userId != authorId) throw SessionValidationException()
                if (session.postId != postId) throw SessionValidationException()
                if (System.currentTimeMillis() > session.expiresAt) throw SessionExpiredException()

                val post = transaction.get(postRef).data?.toPost() ?: throw PostNotFoundException()

                if (post.sessionId != sessionId) throw SessionValidationException()

                val now = System.currentTimeMillis()
                val newGeneration = post.generation + 1
                val isChainComplete = newGeneration > post.maxGenerations
                val newPost = Post(
                    id = newPostRef.id,
                    chainId = post.chainId,
                    authorId = authorId,
                    authorName = authorName,
                    avatarUrl = avatarUrl,
                    content = content,
                    createdAt = now,
                    updatedAt = now,
                    status = PostStatus.AVAILABLE,
                    sessionId = null,
                    generation = newGeneration,
                    maxGenerations = post.maxGenerations,
                    textTimeLimit = post.textTimeLimit,
                    drawingTimeLimit = post.drawingTimeLimit,
                )
                val chainRef = collection.firestore
                    .collection(CHAINS_COLLECTION)
                    .document(post.chainId)
                val contributionRef = collection.firestore
                    .collection(USERS_COLLECTION)
                    .document(authorId)
                    .collection(CONTRIBUTIONS_COLLECTION)
                    .document(newPostRef.id)

                transaction.update(sessionRef, FIELD_STATUS, GameSessionStatus.COMPLETED.name)
                transaction.update(userRef, FIELD_SESSION_ID, null)
                transaction.update(
                    postRef, mapOf(
                        FIELD_STATUS to PostStatus.COMPLETED.name,
                        FIELD_SESSION_ID to null,
                    )
                )
                transaction.update(
                    chainRef, mapOf(
                        FIELD_GENERATION to newGeneration,
                        FIELD_CHAIN_STATUS to if (isChainComplete) ChainStatus.COMPLETED.name else ChainStatus.ACTIVE.name,
                        FIELD_COMPLETED_AT to if (isChainComplete) now else null,
                    )
                )

                transaction.set(newPostRef, newPost.toMap())
                transaction.set(contributionRef, newPost.toMap())
            }.await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            throw e.toAppException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val POSTS_COLLECTION = "posts"
        const val CHAINS_COLLECTION = "chains"
        const val USERS_COLLECTION = "users"
        const val CONTRIBUTIONS_COLLECTION = "contributions"
        const val FIELD_STATUS = "status"
        const val FIELD_SESSION_ID = "sessionId"
        const val FIELD_GENERATION = "generation"
        const val FIELD_CHAIN_STATUS = "status"
        const val FIELD_COMPLETED_AT = "completedAt"
        const val NETWORK_BUFFER_MS = 25_000L
    }
}