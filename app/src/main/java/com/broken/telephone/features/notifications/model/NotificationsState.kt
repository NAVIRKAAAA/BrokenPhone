package com.broken.telephone.features.notifications.model

import com.broken.telephone.domain.settings.NotificationType

data class NotificationsState(
    val enabledNotifications: List<NotificationType> = NotificationType.entries,
)
