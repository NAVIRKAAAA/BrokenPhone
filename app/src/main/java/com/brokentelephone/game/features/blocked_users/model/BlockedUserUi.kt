package com.brokentelephone.game.features.blocked_users.model

import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.User

data class BlockedUserUi(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val createdAt: Long,
)

fun User.toBlockedUserUi() = BlockedUserUi(
    id = id,
    name = username,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
)

fun BlockedUser.toUi(name: String, avatarUrl: String?) = BlockedUserUi(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
)
