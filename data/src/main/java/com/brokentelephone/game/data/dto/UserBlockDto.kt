package com.brokentelephone.game.data.dto

import com.brokentelephone.game.domain.user.BlockedUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserBlockDto(
    @SerialName("blocker_id") val blockerId: String,
    @SerialName("blocked_id") val blockedId: String,
    @SerialName("created_at") val createdAt: Long,
)

fun UserBlockDto.toBlockedUser() = BlockedUser(
    id = blockedId,
    userId = blockedId,
    createdAt = createdAt,
)
