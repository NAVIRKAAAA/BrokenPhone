package com.brokentelephone.game.features.notifications.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.features.notifications.NotificationsScreen
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.notifications_api.NotificationsNavigationApi
import com.brokentelephone.game.notifications_api.NotificationsRoute

class NotificationsNavigationApiImpl : NotificationsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<NotificationsRoute>(
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) {
            NotificationsScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}
