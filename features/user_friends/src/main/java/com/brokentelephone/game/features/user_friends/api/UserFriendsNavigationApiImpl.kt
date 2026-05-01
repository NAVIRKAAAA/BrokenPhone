package com.brokentelephone.game.features.user_friends.api

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.features.user_friends.UserFriendsScreen
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.user_details_api.UserDetailsRoute
import com.brokentelephone.game.user_friends_api.UserFriendsNavigationApi
import com.brokentelephone.game.user_friends_api.UserFriendsRoute

class UserFriendsNavigationApiImpl : UserFriendsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<UserFriendsRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                )
            },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<UserFriendsRoute>()
            UserFriendsScreen(
                userId = route.userId,
                onBackClick = navController::safePopBackStack,
                onUserClick = { userId ->
                    navController.navigateSingle(UserDetailsRoute(userId = userId))
                },
            )
        }
    }
}
