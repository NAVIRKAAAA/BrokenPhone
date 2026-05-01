package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow

class GetCurrentUserUseCase(
    private val userSession: UserSession,
) {
    fun execute(): Flow<User?> {
        return userSession.user
    }
}
