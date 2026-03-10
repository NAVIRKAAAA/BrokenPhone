package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.repository.GamesRepository
import com.brokentelephone.game.domain.user.GameHistoryEntry
import com.brokentelephone.game.domain.user.GameStatus
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
