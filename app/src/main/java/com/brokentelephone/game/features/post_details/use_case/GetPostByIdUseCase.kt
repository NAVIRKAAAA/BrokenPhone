package com.brokentelephone.game.features.post_details.use_case

import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.dashboard.model.toUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetPostByIdUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(id: String): Flow<PostUi?> {
        return repository.getPostById(id).map { it?.toUi() }.flowOn(Dispatchers.IO)
    }


}
