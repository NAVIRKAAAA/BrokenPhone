package com.brokentelephone.game.main.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InitializeSessionUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(): AppResult<Flow<AuthState>> {
        return handler.handle(Dispatchers.IO) {
            userSession.initialize()
            return@handle userSession.state.map { it.authState }
        }
    }
}
