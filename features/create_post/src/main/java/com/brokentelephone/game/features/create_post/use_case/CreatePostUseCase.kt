package com.brokentelephone.game.features.create_post.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class CreatePostUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler
) {

    suspend fun execute(
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ) : AppResult<Unit> {
        return handler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val authState = userSession.authState.first()

            val user = authState.getUserOrNull() ?: throw UnauthorizedException()

            repository.createPost(
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
                text = text,
                maxGenerations = maxGenerations,
                textTimeLimit = textTimeLimit,
                drawingTimeLimit = drawingTimeLimit,
            )
        }
    }
}
