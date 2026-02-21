package com.broken.telephone.domain.repository

interface NotInterestedRepository {

    suspend fun notInterested(userId: String, postParentId: String)

}
