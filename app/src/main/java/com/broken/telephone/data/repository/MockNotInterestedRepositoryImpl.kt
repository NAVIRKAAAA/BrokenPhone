package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.NotInterestedRepository
import kotlinx.coroutines.delay

class MockNotInterestedRepositoryImpl : NotInterestedRepository {

    override suspend fun notInterested(userId: String, postParentId: String) {
        delay(1500)
    }

}
