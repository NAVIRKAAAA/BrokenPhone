package com.brokentelephone.game.features.notifications.model

import com.brokentelephone.game.domain.model.settings.NotificationType

data class NotificationSettingsState(
    val notifications: List<NotificationType> = NotificationType.entries,
    val isNotificationPermissionGranted: Boolean = false,
    val showRationaleDialog: Boolean = false,
)
