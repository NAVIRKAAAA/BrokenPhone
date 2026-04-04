package com.brokentelephone.game.nav_api

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigateSingle(
    route: NavigationRoute,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        navigate(route) { builder() }
    }
}

fun NavController.safePopBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}

fun NavController.navigateSaved(route: NavigationRoute, mainGraphRoute: NavigationRoute) {
    navigate(route) {
        popUpTo(mainGraphRoute) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
