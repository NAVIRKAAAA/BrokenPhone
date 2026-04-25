package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class JoinSessionUseCase(
    private val repository: GameSessionRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(postId: String): AppResult<GameSession> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()

            repository.joinSession(postId, user.id)
        }
    }
}
