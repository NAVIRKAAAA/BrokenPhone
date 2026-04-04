package com.brokentelephone.game.sign_in_api

import com.brokentelephone.game.nav_api.NavigationApi

interface SignInNavigationApi : NavigationApi {
    fun createRoute(email: String) : SignInRoute
}
