package com.brokentelephone.game.nav_api

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

interface NavigationApi {
    val route: NavigationRoute

    fun screen(navController: NavController, builder: NavGraphBuilder)
}
