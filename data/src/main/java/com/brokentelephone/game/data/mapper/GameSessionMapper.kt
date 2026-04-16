package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.GameSessionDto
import com.brokentelephone.game.data.ext.toMillis
import com.brokentelephone.game.data.ext.toTimestamp
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.session.GameSessionStatus
import com.google.firebase.Timestamp

fun GameSessionDto.toGameSession(): GameSession =
    GameSession(
        id = id,
        userId = userId,
        postId = postId,
        lockedAt = lockedAt,
        expiresAt = expiresAt,
        status = runCatching { GameSessionStatus.valueOf(status) }.getOrDefault(GameSessionStatus.ACTIVE),
    )

fun GameSession.toGameSessionDto(): GameSessionDto =
    GameSessionDto(
        id = id,
        userId = userId,
        postId = postId,
        lockedAt = lockedAt,
        expiresAt = expiresAt,
        status = status.name,
    )

fun GameSession.toMap() = mapOf(
    "id" to id,
    "userId" to userId,
    "postId" to postId,
    "lockedAt" to lockedAt.toTimestamp(),
    "expiresAt" to expiresAt.toTimestamp(),
    "status" to status.name,
)

fun Map<String, Any?>.toGameSession(): GameSession? {
    return try {
        GameSession(
            id = this["id"] as? String ?: return null,
            userId = this["userId"] as? String ?: return null,
            postId = this["postId"] as? String ?: return null,
            lockedAt = (this["lockedAt"] as? Timestamp)?.toMillis() ?: return null,
            expiresAt = (this["expiresAt"] as? Timestamp)?.toMillis() ?: return null,
            status = (this["status"] as? String)
                ?.let { runCatching { GameSessionStatus.valueOf(it) }.getOrNull() }
                ?: return null,
        )
    } catch (_: Exception) {
        null
    }
}
