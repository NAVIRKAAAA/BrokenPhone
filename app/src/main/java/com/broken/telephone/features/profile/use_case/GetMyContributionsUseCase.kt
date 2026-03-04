package com.broken.telephone.features.profile.use_case

import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMyContributionsUseCase(
    private val repository: PostRepository,
) {
    operator fun invoke(userId: String): Flow<List<PostUi>> =
        repository.getUserContributions(userId).map { posts -> posts.map { it.toUi() } }
}
