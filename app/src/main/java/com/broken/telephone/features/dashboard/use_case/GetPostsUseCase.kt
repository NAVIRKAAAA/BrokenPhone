package com.broken.telephone.features.dashboard.use_case

import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPostsUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(): Flow<List<PostUi>> = repository.getPosts().map { posts ->
        posts.map { it.toUi() }
    }

}
