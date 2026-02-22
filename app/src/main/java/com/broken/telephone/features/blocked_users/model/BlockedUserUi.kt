package com.broken.telephone.features.blocked_users.model

import com.broken.telephone.domain.user.BlockedUser

data class BlockedUserUi(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val createdAt: Long,
)

fun BlockedUser.toUi(name: String, avatarUrl: String?) = BlockedUserUi(
    id = id,
    name = name,
    avatarUrl = avatarUrl,
    createdAt = createdAt,
)
