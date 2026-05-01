package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameSessionDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("post_id") val postId: String,
    @SerialName("locked_at") val lockedAt: Long,
    @SerialName("expires_at") val expiresAt: Long,
    @SerialName("status") val status: String,
)
