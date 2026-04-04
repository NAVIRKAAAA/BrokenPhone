package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn

class GetPostByIdUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler,
) {

    operator fun invoke(id: String): Flow<Post> {
        return repository.getPostById(id).flowOn(Dispatchers.IO)
    }

    suspend fun executeWithResult(id: String): AppResult<Post> {
        return handler.handle(Dispatchers.IO) {
            repository.getPostById(id).firstOrNull() ?: throw PostNotFoundException()
        }
    }
}
