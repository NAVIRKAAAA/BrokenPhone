package com.brokentelephone.game.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FriendshipDto(
    @SerialName("users") val user: UserDto,
)
