package com.brokentelephone.game.features.notifications_settings.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers

class UpdateNotificationsUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler
) {
    suspend fun execute(notifications: List<NotificationType>) : AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            userSession.updateNotifications(notifications)
        }
    }
}
