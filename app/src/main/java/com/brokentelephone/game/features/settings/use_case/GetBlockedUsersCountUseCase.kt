package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetBlockedUsersCountUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<Int> {
        return userSession.authState.map {
            val user = it.getUserOrNull()
            user?.blockedUserIds?.size ?: 0
        }
    }
}