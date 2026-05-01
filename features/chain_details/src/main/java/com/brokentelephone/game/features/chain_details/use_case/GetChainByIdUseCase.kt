package com.brokentelephone.game.features.chain_details.use_case

import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers

class GetChainByIdUseCase(
    private val repository: PostRepository,
    private val handler: ApiHandler
) {

    suspend fun execute(chainId: String): AppResult<List<PostUi>> {
        return handler.handle(Dispatchers.IO) {
            val entries = repository.getChainById(chainId)
            entries.map { entry -> entry.toUi() }
        }
    }

}
