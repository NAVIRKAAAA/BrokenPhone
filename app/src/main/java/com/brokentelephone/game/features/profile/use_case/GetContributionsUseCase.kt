package com.brokentelephone.game.features.profile.use_case

import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class GetContributionsUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler,
    private val userSession: UserSession,
) {
    suspend fun execute(): AppResult<List<Post>> {
        return handler.handle(Dispatchers.IO) {
            val userId = userSession.authState.first().getUserOrNull()?.id ?: throw UnauthorizedException()
            repository.loadContributions(userId)
        }
    }
}
