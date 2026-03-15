package com.brokentelephone.game.features.describe_drawing.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class SubmitDescriptionUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val apiHandler: ApiHandler,
) {

    suspend fun execute(postId: String, text: String): AppResult<Unit> {
        return apiHandler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val user = userSession.authState.first().getUserOrNull() ?: throw UnauthorizedException()

            repository.submitContinuation(
                postId = postId,
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
                content = PostContent.Text(text = text),
            )
        }
    }
}
