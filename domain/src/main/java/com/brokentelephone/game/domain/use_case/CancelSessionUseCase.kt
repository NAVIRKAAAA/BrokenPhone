package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers

class CancelSessionUseCase(
    private val repository: GameSessionRepository,
    private val userSession: UserSession,
    private val apiHandler: ApiHandler,
) {

    suspend fun execute(sessionId: String): AppResult<Unit> {
        return apiHandler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()

            repository.cancelSession(
                sessionId = sessionId,
                userId = userId,
            )
        }
    }
}
