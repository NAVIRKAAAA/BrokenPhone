package com.brokentelephone.game.features.settings.api

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
import com.brokentelephone.game.account_settings_api.AccountSettingsRoute
import com.brokentelephone.game.features.settings.SettingsScreen
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.settings_api.SettingsNavigationApi
import com.brokentelephone.game.settings_api.SettingsRoute

class SettingsNavigationApiImpl : SettingsNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<SettingsRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("AccountSettings") == true
                    || route?.contains("NotificationSettings") == true
                    || route?.contains("Language") == true
                    || route?.contains("Theme") == true
                    || route?.contains("BlockedUsers") == true
                ) {
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
                if (route?.contains("AccountSettings") == true
                    || route?.contains("NotificationSettings") == true
                    || route?.contains("Language") == true
                    || route?.contains("Theme") == true
                    || route?.contains("BlockedUsers") == true
                ) {
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
            SettingsScreen(
                onBackClick = navController::safePopBackStack,
                onNavigateToWelcome = {
                    navController.navigate(WelcomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAccountSettingsClick = {
                    navController.navigateSingle(AccountSettingsRoute)
                },
                onNotificationsClick = {
//                    navController.navigate(NotificationSettingsRoute)
                },
                onLanguageClick = {
//                    navController.navigate(LanguageRoute)
                },
                onThemeClick = {
//                    navController.navigate(ThemeRoute)
                },
                onBlockedUsersClick = {
//                    navController.navigate(BlockedUsersRoute)
                },
                onNavigateToDraw = { route ->
                    navController.navigateSingle(route)
                },
                onNavigateToDescribeDrawing = { route ->
                    navController.navigateSingle(route)
                },
            )
        }
    }
}
