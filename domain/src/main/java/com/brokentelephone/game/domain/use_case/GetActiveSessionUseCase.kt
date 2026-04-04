package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.SessionNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class GetActiveSessionUseCase(
    private val repository: GameSessionRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(sessionId: String): AppResult<GameSession> {
        return handler.handle(Dispatchers.IO) {
            repository.getSession(sessionId).firstOrNull() ?: throw SessionNotFoundException()
        }
    }

    suspend fun execute(): AppResult<GameSession> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()
            val sessionId = user.sessionId ?: throw SessionNotFoundException()
            repository.getSession(sessionId).firstOrNull() ?: throw SessionNotFoundException()
        }
    }
}
