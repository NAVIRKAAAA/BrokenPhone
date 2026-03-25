package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetPostByIdUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler
) {

    operator fun invoke(id: String): Flow<PostUi> {
        return repository.getPostById(id).map { it.toUi() }.flowOn(Dispatchers.IO)
    }

    suspend fun executeWithResult(id: String) : AppResult<PostUi> {
        return handler.handle(Dispatchers.IO) {
            repository.getPostById(id).firstOrNull()?.toUi() ?: throw PostNotFoundException()
        }
    }

}
