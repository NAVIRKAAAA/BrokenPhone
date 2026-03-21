package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCurrentUserUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<User?> {
        return userSession.authState.map { authState ->
            authState.getUserOrNull()
        }
    }
}
