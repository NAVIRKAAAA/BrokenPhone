package com.broken.telephone.features.chain_details.use_case

import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.features.chain_details.model.toUi
import com.broken.telephone.features.dashboard.model.PostUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetChainByPostIdUseCase(
    private val repository: PostRepository,
) {

    operator fun invoke(postId: String): Flow<List<PostUi>> =
        repository.getChainByPostId(postId).map { entries ->
            val maxGenerations = entries.size
            entries.mapIndexed { index, entry ->
                entry.toUi(
                    id = "${postId}_$index",
                    generation = index,
                    maxGenerations = maxGenerations,
                )
            }
        }

}
