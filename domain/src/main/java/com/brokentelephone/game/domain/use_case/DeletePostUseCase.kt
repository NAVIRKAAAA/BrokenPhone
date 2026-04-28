package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class DeletePostUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(postId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.user.firstOrNull() ?: throw UnauthorizedException()
            
            repository.deletePost(userId = user.id, postId = postId)
        }
    }
}
