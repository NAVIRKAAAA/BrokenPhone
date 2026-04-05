package com.brokentelephone.game.features.notifications_settings.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.features.notifications_settings.NotificationSettingsScreen
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.notifications_settings_api.NotificationSettingsRoute
import com.brokentelephone.game.notifications_settings_api.NotificationsSettingsNavigationApi

class NotificationsSettingsNavigationApiImpl : NotificationsSettingsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<NotificationSettingsRoute>(
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
            }
        ) {
            NotificationSettingsScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}
