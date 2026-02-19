package com.broken.telephone.features.post_details.use_case

import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPostByIdUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(id: String): Flow<PostUi?> = repository.getPostById(id).map { it?.toUi() }

}
