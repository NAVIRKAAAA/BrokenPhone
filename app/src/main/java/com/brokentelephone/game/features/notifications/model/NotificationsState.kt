package com.brokentelephone.game.features.notifications.model

import com.brokentelephone.game.domain.settings.NotificationType

data class NotificationsState(
    val enabledNotifications: List<NotificationType> = NotificationType.entries,
    val isNotificationPermissionGranted: Boolean = false,
    val showRationaleDialog: Boolean = false,
)
