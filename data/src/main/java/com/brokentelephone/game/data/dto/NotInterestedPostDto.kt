package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotInterestedPostDto(
    @SerialName("user_id") val userId: String,
    @SerialName("post_id") val postId: String,
    @SerialName("created_at") val createdAt: Long,
)
