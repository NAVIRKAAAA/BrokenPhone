package com.brokentelephone.game.features.account_settings.api

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.account_settings_api.AccountSettingsNavigationApi
import com.brokentelephone.game.account_settings_api.AccountSettingsRoute
import com.brokentelephone.game.features.account_settings.AccountSettingsScreen
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.nav_api.safePopBackStack

class AccountSettingsNavigationApiImpl : AccountSettingsNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<AccountSettingsRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("BlockedUsers") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                val route = initialState.destination.route
                if (route?.contains("BlockedUsers") == true) {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) {
            AccountSettingsScreen(
                onBackClick = navController::safePopBackStack,
                onNavigateToWelcome = {
                    navController.navigate(WelcomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
