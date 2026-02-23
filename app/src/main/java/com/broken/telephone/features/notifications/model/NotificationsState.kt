package com.broken.telephone.features.notifications.model

import com.broken.telephone.domain.settings.NotificationType

data class NotificationsState(
    val enabledNotifications: List<NotificationType> = NotificationType.entries,
    val isNotificationPermissionGranted: Boolean = false,
    val showRationaleDialog: Boolean = false,
)
