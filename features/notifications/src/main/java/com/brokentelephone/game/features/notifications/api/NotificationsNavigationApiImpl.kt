package com.brokentelephone.game.features.notifications.api

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.chain_details_api.ChainDetailsRoute
import com.brokentelephone.game.features.notifications.NotificationsScreen
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.notification_details_api.NotificationDetailsRoute
import com.brokentelephone.game.notifications_api.NotificationsNavigationApi
import com.brokentelephone.game.notifications_api.NotificationsRoute
import com.brokentelephone.game.user_details_api.UserDetailsRoute

class NotificationsNavigationApiImpl : NotificationsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<NotificationsRoute>(
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                val to = targetState.destination.route
                if (to?.contains("ChainDetails") == true || to?.contains("NotificationDetails") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                val from = initialState.destination.route
                if (from?.contains("ChainDetails") == true || from?.contains("UserDetails") == true || from?.contains("NotificationDetails") == true) {
                    EnterTransition.None
                } else {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300)
                    )
                }
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
                onNavigateToUserDetails = { userId ->
                    navController.navigateSingle(UserDetailsRoute(userId = userId))
                },
                onNavigateToChainDetails = { postId, userId ->
                    navController.navigateSingle(
                        ChainDetailsRoute(
                            postId = postId,
                            userId = userId
                        )
                    )
                },
                onNavigateToNotificationDetails = { notificationId ->
                    navController.navigateSingle(NotificationDetailsRoute(notificationId = notificationId))
                },
            )
        }
    }
}
