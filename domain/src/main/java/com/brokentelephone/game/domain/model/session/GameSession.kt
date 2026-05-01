package com.brokentelephone.game.domain.model.session

enum class GameSessionStatus {
    ACTIVE,
    CANCELLED,
    AUTO_CANCELLED,
    COMPLETED,
}

data class GameSession(
    val id: String,
    val userId: String,
    val postId: String,
    val lockedAt: Long,
    val expiresAt: Long,
    val status: GameSessionStatus,
)

