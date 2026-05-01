package com.brokentelephone.game.features.notifications_settings.model

sealed interface NotificationSettingsSideEffect {
    data object RequestPermission : NotificationSettingsSideEffect
    data object OpenNotificationSettings : NotificationSettingsSideEffect
}
