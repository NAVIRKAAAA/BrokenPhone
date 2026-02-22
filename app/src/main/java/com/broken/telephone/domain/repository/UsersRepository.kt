package com.broken.telephone.domain.repository

import com.broken.telephone.domain.user.User

interface UsersRepository {
    suspend fun getUsersById(ids: List<String>): List<User>
}
