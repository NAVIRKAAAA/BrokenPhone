package com.brokentelephone.game.main.use_case

import android.util.Log
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
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
            val authState = userSession.authState.first { it !is AuthState.Loading }
            Log.d("LOG_TAG", "InitializeSessionUseCase: $authState")
            authState
        }
    }
}
