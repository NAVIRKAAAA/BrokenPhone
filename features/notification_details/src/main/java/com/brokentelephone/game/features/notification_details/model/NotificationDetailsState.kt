package com.brokentelephone.game.features.notification_details.model

import com.brokentelephone.game.core.model.notification.NotificationUi

data class NotificationDetailsState(
    val isLoading: Boolean = true,
    val notificationUi: NotificationUi.News? = null
)
