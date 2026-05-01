package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.repository.NotificationsRepository
import kotlinx.coroutines.Dispatchers

class GetNotificationByIdUseCase(
    private val repository: NotificationsRepository,
    private val handler: ApiHandler,
) {
    suspend fun execute(notificationId: String): AppResult<Notification?> {
        return handler.handle(Dispatchers.IO) {
            repository.getNotificationById(notificationId)
        }
    }
}
