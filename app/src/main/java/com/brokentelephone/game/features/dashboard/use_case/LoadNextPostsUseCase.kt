package com.brokentelephone.game.features.dashboard.use_case

import com.brokentelephone.game.data.model.PostsPage
import com.brokentelephone.game.domain.handler.ApiHandler
import com.brokentelephone.game.domain.handler.AppResult
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.features.dashboard.model.DashboardSort
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class LoadNextPostsUseCase(
    private val repository: PostRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(
        afterDoc: DocumentSnapshot,
        pageSize: Int,
        sort: DashboardSort
    ): AppResult<PostsPage> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()

            repository.loadNextPosts(afterDoc, pageSize, sort, user.id, user.blockedUserIds)
        }
    }
}
