package com.brokentelephone.game.features.profile.api

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
import com.brokentelephone.game.features.profile.ProfileScreen
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import com.brokentelephone.game.profile_api.ProfileRoute

class ProfileNavigationApiImpl : ProfileNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<ProfileRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("PostDetails") == true || route?.contains("EditProfile") == true || route?.contains(
                        "Settings"
                    ) == true
                ) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.Companion.None
                }
            },
            popEnterTransition = {
                val route = initialState.destination.route
                if (route?.contains("PostDetails") == true || route?.contains("EditProfile") == true || route?.contains(
                        "Settings"
                    ) == true
                ) {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                } else {
                    EnterTransition.Companion.None
                }
            },
            popExitTransition = { ExitTransition.Companion.None }
        ) {
            ProfileScreen(
                onPostClick = { postId, userId ->
//                        navController.navigateSingle(Routes.ChainDetails(postId = postId, userId = userId))
                },
                onSignInClick = {
//                        navController.navigateSingle(Routes.SignIn())
                },
                onGetStartedClick = {
//                        navController.navigateSingle(Routes.SignUp)
                },
                onFriendsClick = {
//                        navController.navigateSingle(Routes.Friends)
                },
                onEditClick = {
//                        navController.navigateSingle(Routes.EditProfile)
                },
                onSettingsClick = {
//                        navController.navigateSingle(Routes.Settings)
                },
            )
        }
    }
}