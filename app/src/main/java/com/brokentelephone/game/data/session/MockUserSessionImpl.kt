package com.brokentelephone.game.data.session

import com.brokentelephone.game.domain.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
                notifications = NotificationType.entries,
                onboardingStep = OnboardingStep.CHOOSE_AVATAR,
            )
        )
    )

    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override suspend fun initialize() {
        return
    }

//    override suspend fun setupGuest() {
//        val createdAt = System.currentTimeMillis()
//        val suffix = createdAt.toString().takeLast(5)
//        val avatarUrl = Avatars.all.random().url
//
//        _authState.value = AuthState.Guest(
//            user = User(
//                id = "guest_${UUID.randomUUID()}",
//                username = "Guest #$suffix",
//                email = "",
//                avatarUrl = avatarUrl,
//                createdAt = createdAt,
//                updatedAt = createdAt,
//                authProvider = AuthProvider.GUEST,
//            )
//        )
//    }

    override suspend fun updateUsername(username: String) {
        val user = _authState.value.getUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Auth(
                user.copy(
                    username = username,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun updateAvatar(avatarUrl: String) {
        val user = _authState.value.getUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Auth(
                user.copy(
                    avatarUrl = avatarUrl,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun completeAvatarStep(avatarUrl: String) {
        return
    }

    override suspend fun completeUsernameStep(username: String) {
        return
    }

    private val _blockedUsers = MutableStateFlow(
        listOf(
            BlockedUser(
                id = "block_1",
                userId = "user_2",
                createdAt = System.currentTimeMillis() - 86_400_000
            ),
            BlockedUser(
                id = "block_2",
                userId = "user_3",
                createdAt = System.currentTimeMillis() - 172_800_000
            ),
            BlockedUser(
                id = "block_3",
                userId = "user_4",
                createdAt = System.currentTimeMillis() - 259_200_000
            ),
        )
    )

    override suspend fun blockUser(userId: String) {
        delay(1500)
        val newBlock = BlockedUser(
            id = "block_${System.currentTimeMillis()}",
            userId = userId,
            createdAt = System.currentTimeMillis(),
        )
        _blockedUsers.update { list -> list + newBlock }
    }

    override suspend fun unblockUser(userId: String) {
        delay(1500)
        _blockedUsers.update { list -> list.filter { it.id != userId } }
    }

    override suspend fun updateNotifications(notifications: List<NotificationType>) {
        val user = _authState.value.getUserOrNull()
        if (user != null) {
            _authState.value = AuthState.Auth(user.copy(notifications = notifications))
        }
    }

    override suspend fun signOut() {
        delay(1500)
        _authState.value = AuthState.NotAuth
    }

    override suspend fun deleteAccount() {
        delay(1500)
        _authState.value = AuthState.NotAuth
    }
}
