package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportDto(
    @SerialName("reporter_id") val reporterId: String,
    @SerialName("target_id") val targetId: String,
    @SerialName("target_type") val targetType: String,
    @SerialName("report_subtype") val reportSubtype: String,
    @SerialName("created_at") val createdAt: Long,
)
