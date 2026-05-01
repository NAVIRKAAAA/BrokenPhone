package com.brokentelephone.game.features.confirm_sign_up_api

import com.brokentelephone.game.nav_api.NavigationRoute
import kotlinx.serialization.Serializable

@Serializable
data class ConfirmSignUpRoute(val email: String) : NavigationRoute()
