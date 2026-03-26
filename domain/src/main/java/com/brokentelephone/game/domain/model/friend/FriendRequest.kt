package com.brokentelephone.game.domain.model.friend

enum class FriendRequestStatus {
    PENDING,
    ACCEPTED,
    CANCELLED_BY_SENDER,
    DECLINED_BY_RECEIVER,
}

data class FriendRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val createdAt: Long,
    val status: FriendRequestStatus,
)
