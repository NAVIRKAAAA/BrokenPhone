package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.essentials.exceptions.auth.SessionNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class GetActiveSessionUseCase(
    private val repository: GameSessionRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(sessionId: String): AppResult<GameSession> {
        return handler.handle(Dispatchers.IO) {
            repository.getSession(sessionId).firstOrNull() ?: throw SessionNotFoundException()
        }
    }
}
