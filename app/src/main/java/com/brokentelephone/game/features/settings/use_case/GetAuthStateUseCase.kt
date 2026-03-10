package com.brokentelephone.game.features.settings.use_case

import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow

class GetAuthStateUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<AuthState> = userSession.authState
}
