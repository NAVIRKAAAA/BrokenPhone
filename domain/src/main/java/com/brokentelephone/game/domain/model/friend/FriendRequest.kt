package com.brokentelephone.game.domain.model.friend

data class FriendRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val createdAt: Long,
)
