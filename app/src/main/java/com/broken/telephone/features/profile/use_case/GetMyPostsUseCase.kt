package com.broken.telephone.features.profile.use_case

import com.broken.telephone.domain.user.UserSession
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.dashboard.model.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMyPostsUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<List<PostUi>> {
        return userSession.getMyPosts().map { posts -> posts.map { it.toUi() } }
    }
}
