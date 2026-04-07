package com.brokentelephone.game.features.notification_details.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.features.notification_details.NotificationDetailsScreen
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.notification_details_api.NotificationDetailsNavigationApi
import com.brokentelephone.game.notification_details_api.NotificationDetailsRoute

class NotificationDetailsNavigationApiImpl : NotificationDetailsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<NotificationDetailsRoute>(
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
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<NotificationDetailsRoute>()
            NotificationDetailsScreen(
                notificationId = route.notificationId,
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}
