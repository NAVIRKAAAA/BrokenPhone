package com.brokentelephone.game.features.describe_drawing.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class SubmitDescriptionUseCase(
    private val repository: GameSessionRepository,
    private val userSession: UserSession,
    private val apiHandler: ApiHandler,
) {

    suspend fun execute(sessionId: String, text: String): AppResult<Unit> {
        return apiHandler.handle(dispatcher = Dispatchers.IO) {
            val user = userSession.user.firstOrNull() ?: throw UnauthorizedException()

            repository.completeSession(
                sessionId = sessionId,
                authorId = user.id,
                content = PostContent.Text(text = text),
            )
        }
    }
}
