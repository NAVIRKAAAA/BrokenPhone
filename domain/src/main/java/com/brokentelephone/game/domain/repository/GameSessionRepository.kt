package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSession
import kotlinx.coroutines.flow.Flow

interface GameSessionRepository {

    fun getSession(sessionId: String): Flow<GameSession>

    suspend fun joinSession(postId: String, userId: String, timeLimit: Int): GameSession

    suspend fun cancelSession(sessionId: String, postId: String, userId: String)

    suspend fun completeSession(
        sessionId: String,
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
    )

}