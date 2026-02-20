package com.broken.telephone.navigation.utils

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import com.broken.telephone.navigation.routes.Routes

fun NavController.navigateSingle(
    routes: Routes,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        navigate(routes) { builder() }
    }
}

fun NavController.safePopBackStack() {
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
        popBackStack()
    }
}

fun NavController.navigateSaved(route: Routes) {
    this.navigate(route) {
        popUpTo(Routes.Dashboard) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}