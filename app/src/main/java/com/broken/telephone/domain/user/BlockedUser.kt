package com.broken.telephone.domain.user

data class BlockedUser(
    val id: String,
    val userId: String,
    val createdAt: Long,
)
