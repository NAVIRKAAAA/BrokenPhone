package com.brokentelephone.game.features.notifications.model

import com.brokentelephone.game.core.model.notification.NotificationUi
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.domain.model.notification.NotificationFilter

data class NotificationsState(
    val isLoading: Boolean = true,
    val isLoadingByFilter: Boolean = false,
    val groupedNotifications: Map<NotificationDateGroup, List<NotificationUi>> = emptyMap(),
    val isRefreshing: Boolean = false,
    val selectedFilter: NotificationFilter = NotificationFilter.ALL,
    val user: UserUi? = null,
)
