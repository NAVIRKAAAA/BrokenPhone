package com.brokentelephone.game.notification_details_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDetailsRoute(val notificationId: String) : NavigationRoute()
