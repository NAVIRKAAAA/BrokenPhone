package com.brokentelephone.game.features.add_friend.api

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
import com.brokentelephone.game.add_friend_api.AddFriendNavigationApi
import com.brokentelephone.game.add_friend_api.AddFriendRoute
import com.brokentelephone.game.features.add_friend.AddFriendScreen
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.user_details_api.UserDetailsRoute

class AddFriendNavigationApiImpl : AddFriendNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<AddFriendRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                if (targetState.destination.route?.contains("UserDetails") == true) {
                    ExitTransition.None
                } else {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("UserDetails") == true) {
                    EnterTransition.None
                } else {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                }
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) {
            AddFriendScreen(
                onBackClick = navController::safePopBackStack,
                onUserClick = { userId ->
                    navController.navigateSingle(UserDetailsRoute(userId = userId))
                },
            )
        }
    }
}
