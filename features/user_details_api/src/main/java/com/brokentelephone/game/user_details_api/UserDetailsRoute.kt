package com.brokentelephone.game.user_details_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsRoute(val userId: String) : NavigationRoute()
