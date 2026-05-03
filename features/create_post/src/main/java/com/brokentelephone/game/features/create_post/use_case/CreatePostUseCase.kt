package com.brokentelephone.game.features.create_post.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.ImageUploadException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class CreatePostUseCase(
    private val repository: PostRepository,
    private val imageStorage: ImageStorage,
    private val userSession: UserSession,
    private val handler: ApiHandler
) {

    suspend fun execute(
        content: PostContent,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ): AppResult<Unit> {
        return handler.handle(dispatcher = Dispatchers.IO, maxRetries = 0) {
            val user = userSession.getAuthUserOrNull().firstOrNull() ?: throw UnauthorizedException()

            val resolvedContent = when (content) {
                is PostContent.Text -> content
                is PostContent.Drawing -> {
                    val localPath = content.localPath ?: throw ImageUploadException()
                    val imageUrl = try {
                        imageStorage.uploadImage(localPath)
                    } catch (_: Exception) {
                        throw ImageUploadException()
                    }
                    PostContent.Drawing(imageUrl = imageUrl)
                }
            }

            repository.createPost(
                authorId = user.id,
                authorName = user.username,
                avatarUrl = user.avatarUrl,
                content = resolvedContent,
                maxGenerations = maxGenerations,
                textTimeLimit = textTimeLimit,
                drawingTimeLimit = drawingTimeLimit,
            )
        }
    }
}
