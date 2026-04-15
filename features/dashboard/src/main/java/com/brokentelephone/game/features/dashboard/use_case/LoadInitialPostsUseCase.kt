package com.brokentelephone.game.features.dashboard.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull


class LoadInitialPostsUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(pageSize: Int, sort: DashboardSort): AppResult<PostsPage> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()

            repository.loadInitialPosts(
                pageSize,
                sort,
                user.id,
//                user.blockedUserIds,
//                user.blockedBy,
                blockedUsersIds = listOf(),
                blockedBy = listOf(),
                notInterestedPostIds = user.notInterestedPostIds
            )
        }
    }
}
