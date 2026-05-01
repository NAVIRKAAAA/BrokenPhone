package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers

class MarkNotificationAsReadUseCase(
    private val repository: NotificationsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(notificationId: String): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()

            repository.markNotificationAsRead(userId, notificationId)
        }
    }
}
