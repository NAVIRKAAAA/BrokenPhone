package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @SerialName("id") val id: String,
    @SerialName("receiver_ids") val receiverIds: List<String>,
    @SerialName("type") val type: String,
    @SerialName("created_at") val createdAt: Long,

    // Friends
    @SerialName("request_id") val requestId: String? = null,
    @SerialName("sender_id") val senderId: String? = null,
    @SerialName("sender_username") val senderUsername: String? = null,
    @SerialName("sender_avatar_url") val senderAvatarUrl: String? = null,
    @SerialName("friends_type") val friendsType: String? = null,

    // ChainInfo
    @SerialName("chain_id") val chainId: String? = null,
    @SerialName("post_id") val postId: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("body") val body: String? = null,
)
