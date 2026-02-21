package com.broken.telephone.domain.repository

interface BlockRepository {

    suspend fun block(userId: String, blockedUserId: String)

}
