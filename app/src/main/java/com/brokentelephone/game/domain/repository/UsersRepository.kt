package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.user.User

interface UsersRepository {
    suspend fun getUsersById(ids: List<String>): List<User>
}
