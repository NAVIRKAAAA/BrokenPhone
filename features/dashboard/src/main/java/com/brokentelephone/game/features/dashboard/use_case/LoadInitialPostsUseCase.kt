package com.brokentelephone.game.features.dashboard.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers


class LoadInitialPostsUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(pageSize: Int, sort: DashboardSort): AppResult<PostsPage> {
        return handler.handle(Dispatchers.IO) {
            val excludedUserIds = userSession.getExcludedUserIds()
            repository.loadInitialPosts(pageSize, sort, excludedUserIds)
        }
    }
}
