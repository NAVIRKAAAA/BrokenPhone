package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.FriendRequestDto
import com.brokentelephone.game.domain.model.friend.FriendRequest
import com.brokentelephone.game.domain.model.friend.FriendRequestStatus

fun FriendRequestDto.toDomain(): FriendRequest? {
    val status = runCatching { FriendRequestStatus.valueOf(status) }.getOrNull() ?: return null

    return FriendRequest(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        createdAt = createdAt,
        status = status,
    )
}
