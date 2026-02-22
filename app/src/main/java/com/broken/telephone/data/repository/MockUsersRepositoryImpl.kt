package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.UsersRepository
import com.broken.telephone.domain.user.User

class MockUsersRepositoryImpl : UsersRepository {

    private val users = listOf(
        User(id = "user_1", username = "Alice", email = "alice@example.com", avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png", createdAt = 0, updatedAt = 0),
        User(id = "user_2", username = "Bob", email = "bob@example.com", avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png", createdAt = 0, updatedAt = 0),
        User(id = "user_3", username = "Charlie", email = "charlie@example.com", avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png", createdAt = 0, updatedAt = 0),
        User(id = "user_4", username = "Diana", email = "diana@example.com", avatarUrl = null, createdAt = 0, updatedAt = 0),
        User(id = "user_5", username = "Eve", email = "eve@example.com", avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png", createdAt = 0, updatedAt = 0),
    )

    override suspend fun getUsersById(ids: List<String>): List<User> {
        return users.filter { it.id in ids }
    }
}
