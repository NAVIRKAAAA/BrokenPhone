package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendRequestDto(
    @SerialName("id") val id: String,
    @SerialName("sender_id") val senderId: String,
    @SerialName("receiver_id") val receiverId: String,
    @SerialName("created_at") val createdAt: Long,
)
