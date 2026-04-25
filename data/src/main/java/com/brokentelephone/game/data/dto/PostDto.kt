package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostAuthorDto(
    @SerialName("username") val username: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

@Serializable
data class PostDto(
    @SerialName("id") val id: String,
    @SerialName("chain_id") val chainId: String,
    @SerialName("author_id") val authorId: String,
    @SerialName("author_name") val authorName: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("users") val author: PostAuthorDto? = null,
    @SerialName("content_type") val contentType: String,
    @SerialName("content_text") val contentText: String? = null,
    @SerialName("content_image_url") val contentImageUrl: String? = null,
    @SerialName("status") val status: String,
    @SerialName("generation") val generation: Int,
    @SerialName("max_generations") val maxGenerations: Int,
    @SerialName("text_time_limit") val textTimeLimit: Int,
    @SerialName("drawing_time_limit") val drawingTimeLimit: Int,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long,
)