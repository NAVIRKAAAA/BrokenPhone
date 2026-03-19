package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSession

interface GameSessionRepository {

    suspend fun joinSession(postId: String, userId: String, timeLimit: Int): GameSession

    suspend fun completeSession(
        sessionId: String,
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
    )

}