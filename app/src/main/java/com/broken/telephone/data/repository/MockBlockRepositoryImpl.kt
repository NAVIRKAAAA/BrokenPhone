package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.BlockRepository
import kotlinx.coroutines.delay

class MockBlockRepositoryImpl : BlockRepository {

    override suspend fun block(userId: String, blockedUserId: String) {
        delay(1500)
    }

}
