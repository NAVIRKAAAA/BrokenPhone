package com.brokentelephone.game.features.notification_details.model

sealed interface NotificationDetailsSideEffect {
    data object NavigateBack : NotificationDetailsSideEffect
}
