package com.brokentelephone.game.data.mapper

import com.brokentelephone.game.data.dto.FriendRequestDto
import com.brokentelephone.game.domain.model.friend.FriendRequest

fun FriendRequestDto.toDomain(): FriendRequest {

    return FriendRequest(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        createdAt = createdAt,
    )
}
