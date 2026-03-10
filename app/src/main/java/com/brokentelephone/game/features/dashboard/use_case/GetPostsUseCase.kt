package com.brokentelephone.game.features.dashboard.use_case

import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPostsUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(): Flow<List<PostUi>> = repository.getPosts().map { posts ->
        posts.map { it.toUi() }
    }

}
