package com.brokentelephone.game.domain.model.friend

// TODO: remove it
enum class FriendRequestStatus {
    PENDING,
    ACCEPTED,
}

data class FriendRequest(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val createdAt: Long,
    val status: FriendRequestStatus,
)
