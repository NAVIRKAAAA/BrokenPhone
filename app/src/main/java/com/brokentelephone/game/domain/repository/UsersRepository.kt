package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.User

interface UsersRepository {
    suspend fun getUsersById(ids: List<String>): List<User>
    suspend fun createUser(id: String, email: String, authProvider: AuthProvider)
}
