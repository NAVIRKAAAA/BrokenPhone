package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class ReportPostUseCase(
    private val repository: ReportsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(postId: String, type: ReportPostType): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()
            repository.report(user.id, postId, type)
        }
    }
}
