package com.brokentelephone.game.features.notifications.model

sealed interface NotificationSettingsSideEffect {
    data object RequestPermission : NotificationSettingsSideEffect
    data object OpenNotificationSettings : NotificationSettingsSideEffect
}
