package com.brokentelephone.game.features.choose_username.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.choose_username_api.ChooseUsernameNavigationApi
import com.brokentelephone.game.choose_username_api.ChooseUsernameRoute
import com.brokentelephone.game.dashboard_api.DashboardNavigationApi
import com.brokentelephone.game.features.choose_username.ChooseUsernameScreen
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack

class ChooseUsernameNavigationApiImpl(
    private val dashboardNavigationApi: DashboardNavigationApi
) : ChooseUsernameNavigationApi {

    override val route = ChooseUsernameRoute

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<ChooseUsernameRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            },
        ) {
            ChooseUsernameScreen(
                onBackClick = navController::safePopBackStack,
                navigateToFeed = {
                    navController.navigateSingle(dashboardNavigationApi.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}