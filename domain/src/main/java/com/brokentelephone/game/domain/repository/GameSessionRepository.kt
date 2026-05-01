package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSession
import kotlinx.coroutines.flow.Flow

interface GameSessionRepository {

    fun getSession(sessionId: String): Flow<GameSession>

    suspend fun joinSession(postId: String, userId: String): GameSession

    suspend fun cancelSession(sessionId: String, userId: String)

    suspend fun completeSession(
        sessionId: String,
        authorId: String,
        content: PostContent,
    )

}