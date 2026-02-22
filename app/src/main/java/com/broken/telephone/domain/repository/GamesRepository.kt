package com.broken.telephone.domain.repository

import com.broken.telephone.domain.user.GameHistoryEntry

interface GamesRepository {
    suspend fun createGame(postId: String, userId: String): GameHistoryEntry
}
