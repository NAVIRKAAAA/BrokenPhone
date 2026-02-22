package com.broken.telephone.domain.user

enum class GameStatus {
    IN_PROGRESS,
    COMPLETED,
    EXPIRED,
    CANCELLED,
}

data class GameHistoryEntry(
    val id: String,
    val postId: String,
    val userId: String,
    val startedAt: Long,
    val finishedAt: Long?,
    val status: GameStatus,
)
