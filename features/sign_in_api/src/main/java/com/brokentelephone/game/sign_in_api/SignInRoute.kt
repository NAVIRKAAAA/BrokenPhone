package com.brokentelephone.game.sign_in_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class SignInRoute(val email: String = "") : NavigationRoute()
