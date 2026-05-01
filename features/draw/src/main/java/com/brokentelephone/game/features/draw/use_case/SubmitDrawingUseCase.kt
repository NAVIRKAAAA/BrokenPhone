package com.brokentelephone.game.features.draw.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.ImageUploadException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers

class SubmitDrawingUseCase(
    private val repository: GameSessionRepository,
    private val imageStorage: ImageStorage,
    private val userSession: UserSession,
    private val apiHandler: ApiHandler
) {

    suspend fun execute(sessionId: String, localPath: String): AppResult<Unit> {
        return apiHandler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()

            val imageUrl = try {
                imageStorage.uploadImage(localPath)
            } catch (_: Exception) {
                throw ImageUploadException()
            }

            repository.completeSession(
                sessionId = sessionId,
                authorId = userId,
                content = PostContent.Drawing(imageUrl = imageUrl),
            )
        }
    }
}
