package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers

class DeletePostUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler
) {
    suspend fun execute(postId: String, parentId: String) : AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            repository.deletePost(postId, parentId)
        }
    }
}
