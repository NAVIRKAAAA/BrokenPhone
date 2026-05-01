package com.brokentelephone.game.forgot_password_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordRoute(val email: String = "") : NavigationRoute()