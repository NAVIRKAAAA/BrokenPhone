package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.repository.NotInterestedRepository
import kotlinx.coroutines.delay

class MockNotInterestedRepositoryImpl : NotInterestedRepository {

    override suspend fun notInterested(userId: String, postParentId: String) {
        delay(1500)
    }

}
