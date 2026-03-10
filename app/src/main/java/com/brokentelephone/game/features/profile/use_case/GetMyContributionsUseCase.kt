package com.brokentelephone.game.features.profile.use_case

import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMyContributionsUseCase(
    private val repository: PostRepository,
) {
    operator fun invoke(userId: String): Flow<List<PostUi>> =
        repository.getUserContributions(userId).map { posts -> posts.map { it.toUi() } }
}
