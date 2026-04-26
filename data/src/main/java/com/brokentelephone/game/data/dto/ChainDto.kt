package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChainDto(
    @SerialName("id") val id: String,
    @SerialName("status") val status: String,
    @SerialName("generation") val generation: Int,
    @SerialName("max_generations") val maxGenerations: Int,
    @SerialName("text_time_limit") val textTimeLimit: Int,
    @SerialName("drawing_time_limit") val drawingTimeLimit: Int,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("completed_at") val completedAt: Long? = null,
)
