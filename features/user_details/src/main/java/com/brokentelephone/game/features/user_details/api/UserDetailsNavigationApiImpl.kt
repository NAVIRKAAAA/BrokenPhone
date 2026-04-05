package com.brokentelephone.game.features.user_details.api

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.chain_details_api.ChainDetailsRoute
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.features.user_details.UserDetailsScreen
import com.brokentelephone.game.nav_api.KEY_FORCE_REFRESH
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.user_details_api.UserDetailsNavigationApi
import com.brokentelephone.game.user_details_api.UserDetailsRoute
import com.brokentelephone.game.user_friends_api.UserFriendsRoute

class UserDetailsNavigationApiImpl : UserDetailsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<UserDetailsRoute>(
            enterTransition = {
                val from = initialState.destination.route
                if (from?.contains("Dashboard") == true || from?.contains("PostDetails") == true || from?.contains(
                        "ChainDetails"
                    ) == true || from?.contains("Friends") == true || from?.contains("AddFriend") == true
                ) {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300)
                    )
                } else {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                }
            },
            exitTransition = {
                val to = targetState.destination.route
                if (to?.contains("UserFriends") == true) {
                    ExitTransition.None
                } else {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            },
            popEnterTransition = {
                val from = initialState.destination.route
                if (from?.contains("UserFriends") == true) {
                    EnterTransition.None
                } else {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                }
            },
            popExitTransition = {
                val to = targetState.destination.route
                if (to?.contains("Dashboard") == true || to?.contains("PostDetails") == true || to?.contains(
                        "ChainDetails"
                    ) == true || to?.contains("Friends") == true || to?.contains("AddFriend") == true
                ) {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<UserDetailsRoute>()
            UserDetailsScreen(
                userId = route.userId,
                onBackClick = navController::safePopBackStack,
                onFriendsClick = { userId ->
                    navController.navigateSingle(UserFriendsRoute(userId = userId))
                },
                onPostClick = { postId, userId ->
                    navController.navigateSingle(
                        ChainDetailsRoute(
                            postId = postId,
                            userId = userId
                        )
                    )
                },
                onNavigateBackWithForceUpdate = {
                    val isDashboardInBackStack = navController.previousBackStackEntry
                        ?.destination?.route?.contains("Dashboard") == true

                    if (isDashboardInBackStack) {
                        navController.getBackStackEntry(DashboardRoute)
                            .savedStateHandle[KEY_FORCE_REFRESH] = true
                    }
                    navController.safePopBackStack()
                },
            )
        }
    }
}
