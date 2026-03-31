package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.User

interface UsersRepository {
    suspend fun getUserById(id: String): User?
    suspend fun getUsersByIds(ids: List<String>): List<User>
    suspend fun getUserByEmail(email: String): User?
    suspend fun searchByUsername(query: String): List<User>
    suspend fun createUser(
        id: String,
        email: String,
        authProvider: AuthProvider
    )

    suspend fun createUser(id: String)

    suspend fun getSuggestedUsers(excludeIds: List<String>): List<User>
}
