package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.GamesRepository
import com.broken.telephone.domain.user.GameHistoryEntry
import com.broken.telephone.domain.user.GameStatus
import kotlinx.coroutines.delay

class MockGamesRepositoryImpl : GamesRepository {

    override suspend fun createGame(postId: String, userId: String): GameHistoryEntry {
        delay(1500)
        return GameHistoryEntry(
            id = "game_${System.currentTimeMillis()}",
            postId = postId,
            userId = userId,
            startedAt = System.currentTimeMillis(),
            finishedAt = null,
            status = GameStatus.IN_PROGRESS,
        )
    }
}
