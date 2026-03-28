package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.report.ReportUserType
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class ReportUserUseCase(
    private val repository: ReportsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(targetUserId: String, type: ReportUserType): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val user = userSession.authState.firstOrNull()?.getUserOrNull()
                ?: throw UnauthorizedException()
            repository.reportUser(user.id, targetUserId, type)
        }
    }
}
