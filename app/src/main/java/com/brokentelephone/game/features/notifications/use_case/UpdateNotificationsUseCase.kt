package com.brokentelephone.game.features.notifications.use_case

import com.brokentelephone.game.domain.settings.NotificationType
import com.brokentelephone.game.domain.user.UserSession

class UpdateNotificationsUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(enabledNotifications: List<NotificationType>) {
        userSession.updateNotifications(enabledNotifications)
    }
}
