package com.brokentelephone.game.features.notifications.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationFilter
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class GetNotificationsByFilterUseCase(
    private val repository: NotificationsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler
) {

    suspend fun execute(filter: NotificationFilter): AppResult<List<Notification>> {
        return handler.handle(Dispatchers.IO) {
            val userId = userSession.getUserId() ?: throw UnauthorizedException()

            when (filter) {
                NotificationFilter.ALL -> repository.getNotifications(userId)
                NotificationFilter.UNREAD -> {

                    val notifications = repository.getNotifications(userId)
                    val readNotificationIds =
                        userSession.getUser().firstOrNull()?.readNotificationIds ?: emptyList()

                    notifications.filterNot { notification ->
                        readNotificationIds.contains(notification.id)
                    }
                }

                else -> repository.getNotifications(userId, filter)
            }
        }
    }

}