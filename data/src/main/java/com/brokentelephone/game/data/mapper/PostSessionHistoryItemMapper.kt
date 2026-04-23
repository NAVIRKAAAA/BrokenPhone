package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.PostSessionHistoryItemDto
import com.brokentelephone.game.domain.model.session.PostSessionHistoryItem
import com.brokentelephone.game.domain.model.session.PostSessionHistoryType

fun PostSessionHistoryItemDto.toPostSessionHistoryItem(): PostSessionHistoryItem? = runCatching {
    PostSessionHistoryItem(
        sessionId = sessionId,
        userId = userId,
        type = PostSessionHistoryType.valueOf(type),
        timestamp = timestamp,
    )
}.getOrNull()

fun PostSessionHistoryItem.toDto(): PostSessionHistoryItemDto = PostSessionHistoryItemDto(
    sessionId = sessionId,
    userId = userId,
    type = type.name,
    timestamp = timestamp,
)