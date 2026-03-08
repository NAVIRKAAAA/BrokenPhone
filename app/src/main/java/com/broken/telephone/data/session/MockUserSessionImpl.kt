package com.broken.telephone.data.session

import com.broken.telephone.domain.settings.NotificationType
import com.broken.telephone.domain.user.AuthProvider
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.BlockedUser
import com.broken.telephone.domain.user.User
import com.broken.telephone.domain.user.UserSession
import com.broken.telephone.features.edit_avatar.model.Avatars
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class MockUserSessionImpl : UserSession {

    private val _authState = MutableStateFlow<AuthState>(
        AuthState.Auth(
            user = User(
                id = "mock-user-id",
                username = "Alex",
                email = "alex@example.com",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                createdAt = 1_700_000_000_000L,
                updatedAt = 1_700_000_000_000L,
                authProvider = AuthProvider.EMAIL,
            )
        )
    )

    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override suspend fun setupGuest() {
        val createdAt = System.currentTimeMillis()
        val suffix = createdAt.toString().takeLast(5)
        val avatarUrl = Avatars.all.random().url

        _authState.value = AuthState.Guest(
            user = User(
                id = "guest_${UUID.randomUUID()}",
                username = "Guest #$suffix",
                email = "",
                avatarUrl = avatarUrl,
                createdAt = createdAt,
                updatedAt = createdAt,
                authProvider = AuthProvider.GUEST,
            )
        )
    }

    override suspend fun updateProfile(username: String) {
        val user = _authState.value.getUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Auth(user.copy(username = username, updatedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun updateAvatar(avatarUrl: String) {
        val user = _authState.value.getUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Auth(user.copy(avatarUrl = avatarUrl, updatedAt = System.currentTimeMillis()))
        }
    }

    private val _blockedUsers = MutableStateFlow(
        listOf(
            BlockedUser(id = "block_1", userId = "user_2", createdAt = System.currentTimeMillis() - 86_400_000),
            BlockedUser(id = "block_2", userId = "user_3", createdAt = System.currentTimeMillis() - 172_800_000),
            BlockedUser(id = "block_3", userId = "user_4", createdAt = System.currentTimeMillis() - 259_200_000),
        )
    )

    override fun getBlockedUsers(): Flow<List<BlockedUser>> = _blockedUsers.asStateFlow()

    override suspend fun blockUser(blockedUserId: String) {
        delay(1500)
        val newBlock = BlockedUser(
            id = "block_${System.currentTimeMillis()}",
            userId = blockedUserId,
            createdAt = System.currentTimeMillis(),
        )
        _blockedUsers.update { list -> list + newBlock }
    }

    override suspend fun unblockUser(blockId: String) {
        delay(1500)
        _blockedUsers.update { list -> list.filter { it.id != blockId } }
    }

    override suspend fun updateNotifications(enabledNotifications: List<NotificationType>) {
        val user = _authState.value.getUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Auth(user.copy(enabledNotifications = enabledNotifications))
        }
    }

    override suspend fun logout() {
        delay(1500)
        _authState.value = AuthState.NotAuth
    }

    override suspend fun deleteAccount() {
        delay(1500)
        _authState.value = AuthState.NotAuth
    }
}
