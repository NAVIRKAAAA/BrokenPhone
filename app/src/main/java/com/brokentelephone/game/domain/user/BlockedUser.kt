package com.brokentelephone.game.domain.user

data class BlockedUser(
    val id: String,
    val userId: String,
    val createdAt: Long,
)
