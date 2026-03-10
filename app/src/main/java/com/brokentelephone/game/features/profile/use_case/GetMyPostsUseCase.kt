package com.brokentelephone.game.features.profile.use_case

import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMyPostsUseCase(
    private val repository: PostRepository,
) {
    operator fun invoke(userId: String): Flow<List<PostUi>> =
        repository.getUserPosts(userId).map { posts -> posts.map { it.toUi() } }
}
