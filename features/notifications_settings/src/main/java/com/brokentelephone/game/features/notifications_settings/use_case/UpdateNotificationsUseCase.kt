package com.brokentelephone.game.features.notifications_settings.use_case

import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.UserSession

class UpdateNotificationsUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(notifications: List<NotificationType>) {
        userSession.updateNotifications(notifications)
    }
}
