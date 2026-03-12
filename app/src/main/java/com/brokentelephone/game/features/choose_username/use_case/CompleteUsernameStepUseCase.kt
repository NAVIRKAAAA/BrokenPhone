package com.brokentelephone.game.features.choose_username.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class CompleteUsernameStepUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(username: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.completeUsernameStep(username)
        }
    }
}
