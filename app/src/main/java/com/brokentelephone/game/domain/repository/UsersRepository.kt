package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.User

interface UsersRepository {
    suspend fun getUserById(id: String): User?
    suspend fun createUser(id: String, email: String, authProvider: AuthProvider)
}
