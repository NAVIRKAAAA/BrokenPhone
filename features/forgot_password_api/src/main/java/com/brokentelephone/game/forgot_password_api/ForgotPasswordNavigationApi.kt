package com.brokentelephone.game.forgot_password_api

import com.brokentelephone.game.nav_api.NavigationApi

interface ForgotPasswordNavigationApi : NavigationApi {
    fun createRoute(email: String) : ForgotPasswordRoute
}
