package com.broken.telephone.features.notifications.use_case

import com.broken.telephone.domain.settings.NotificationType
import com.broken.telephone.domain.user.UserSession

class UpdateNotificationsUseCase(
    private val userSession: UserSession,
) {
    suspend operator fun invoke(enabledNotifications: List<NotificationType>) {
        userSession.updateNotifications(enabledNotifications)
    }
}
