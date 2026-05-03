package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAuthStateUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<AuthState> = userSession.state.map { it.authState }
}
