package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.user.GameHistoryEntry

interface GamesRepository {
    suspend fun createGame(postId: String, userId: String): GameHistoryEntry
}
