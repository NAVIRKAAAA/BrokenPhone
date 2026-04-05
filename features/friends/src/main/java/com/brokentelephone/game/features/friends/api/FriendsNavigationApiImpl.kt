package com.brokentelephone.game.features.friends.api

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
import com.brokentelephone.game.features.friends.FriendsScreen
import com.brokentelephone.game.friends_api.FriendsNavigationApi
import com.brokentelephone.game.friends_api.FriendsRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.user_details_api.UserDetailsRoute

class FriendsNavigationApiImpl : FriendsNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<FriendsRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                if (targetState.destination.route?.contains("AddFriend") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("AddFriend") == true) {
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
            FriendsScreen(
                onBackClick = navController::safePopBackStack,
                onUserClick = { userId ->
                    navController.navigateSingle(UserDetailsRoute(userId = userId))
                },
                onAddFriendClick = {
//                    navController.navigateSingle(AddFriendRoute)
                },
            )
        }
    }
}
