package com.brokentelephone.game.main.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class InitializeSessionUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<AuthState> {
        return handler.handle(Dispatchers.IO) {
            userSession.initialize()
            userSession.authState.first()
        }
    }
}
