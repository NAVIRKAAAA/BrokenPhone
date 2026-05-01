package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers

class ReportPostUseCase(
    private val repository: ReportsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(postId: String, type: ReportPostType): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()

            repository.reportPost(userId, postId, type)
        }
    }
}
