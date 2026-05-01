package com.brokentelephone.game.describe_drawing_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class DescribeDrawingRoute(val sessionId: String) : NavigationRoute()