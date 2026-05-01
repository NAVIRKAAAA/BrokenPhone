package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers

class GetUserCompletedPostsUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(userId: String): AppResult<List<Post>> {
        return handler.handle(Dispatchers.IO) {
            repository.loadUserCompletedPosts(userId)
        }
    }
}
