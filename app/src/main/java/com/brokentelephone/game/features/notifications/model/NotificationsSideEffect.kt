package com.brokentelephone.game.features.notifications.model

sealed interface NotificationsSideEffect {
    data object RequestPermission : NotificationsSideEffect
    data object OpenNotificationSettings : NotificationsSideEffect
}
