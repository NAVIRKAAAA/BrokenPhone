package com.brokentelephone.game.domain.repository

interface NotInterestedRepository {

    suspend fun notInterested(userId: String, postParentId: String)

}
