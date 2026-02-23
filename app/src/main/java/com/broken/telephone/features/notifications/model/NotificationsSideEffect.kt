package com.broken.telephone.features.notifications.model

sealed interface NotificationsSideEffect {
    data object RequestPermission : NotificationsSideEffect
    data object OpenNotificationSettings : NotificationsSideEffect
}
