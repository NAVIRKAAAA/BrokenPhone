package com.brokentelephone.game.features.draw.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class SubmitDrawingUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val apiHandler: ApiHandler
) {

    suspend fun execute(postId: String, localPath: String) : AppResult<Unit> {
        return apiHandler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val user = userSession.authState.first().getUserOrNull() ?: throw UnauthorizedException()

            repository.submitContinuation(
                postId = postId,
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
                content = PostContent.Drawing(localPath = localPath),
            )
        }
    }
}
