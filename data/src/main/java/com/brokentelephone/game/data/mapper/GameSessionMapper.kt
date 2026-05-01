package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.GameSessionDto
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.session.GameSessionStatus

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