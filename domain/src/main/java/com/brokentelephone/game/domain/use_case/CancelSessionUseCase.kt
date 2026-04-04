package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class CancelSessionUseCase(
    private val repository: GameSessionRepository,
    private val userSession: UserSession,
    private val apiHandler: ApiHandler,
) {

    suspend fun execute(sessionId: String, postId: String): AppResult<Unit> {
        return apiHandler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()

            repository.cancelSession(
                sessionId = sessionId,
                postId = postId,
                userId = user.id,
            )
        }
    }
}
