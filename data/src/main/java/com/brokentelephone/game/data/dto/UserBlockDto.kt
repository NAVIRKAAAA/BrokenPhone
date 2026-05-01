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

@Serializable
data class UserBlockUserDto(
    @SerialName("username") val username: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

@Serializable
data class UserBlockWithUserDto(
    @SerialName("blocker_id") val blockerId: String,
    @SerialName("blocked_id") val blockedId: String,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("users") val user: UserBlockUserDto,
)

fun UserBlockWithUserDto.toBlockedUser() = BlockedUser(
    id = blockedId,
    userId = blockedId,
    username = user.username,
    avatarUrl = user.avatarUrl,
    createdAt = createdAt,
)
