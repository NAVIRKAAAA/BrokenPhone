package com.brokentelephone.game.draw_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class DrawRoute(val sessionId: String) : NavigationRoute()