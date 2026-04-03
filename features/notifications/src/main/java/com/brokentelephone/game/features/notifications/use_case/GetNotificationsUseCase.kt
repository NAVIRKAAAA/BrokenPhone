package com.brokentelephone.game.features.notifications.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class GetNotificationsUseCase(
    private val repository: NotificationsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler
) {
    suspend fun execute(): AppResult<List<Notification>> {
        return handler.handle(Dispatchers.IO) {
            val currentUser = userSession.authState.first().getUserOrNull()
                ?: throw UnauthorizedException()

            repository.getNotifications(currentUser.id)
        }
    }
}
