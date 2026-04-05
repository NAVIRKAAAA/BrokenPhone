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
import com.brokentelephone.game.chain_details_api.ChainDetailsRoute
import com.brokentelephone.game.edit_profile_api.EditProfileRoute
import com.brokentelephone.game.features.profile.ProfileScreen
import com.brokentelephone.game.features.sign_up_api.SignUpRoute
import com.brokentelephone.game.friends_api.FriendsRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import com.brokentelephone.game.profile_api.ProfileRoute
import com.brokentelephone.game.settings_api.SettingsRoute
import com.brokentelephone.game.sign_in_api.SignInRoute

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
                    navController.navigateSingle(
                        ChainDetailsRoute(
                            postId = postId,
                            userId = userId
                        )
                    )
                },
                onSignInClick = {
                    navController.navigateSingle(SignInRoute())
                },
                onGetStartedClick = {
                    navController.navigateSingle(SignUpRoute)
                },
                onFriendsClick = {
                    navController.navigateSingle(FriendsRoute)
                },
                onEditClick = {
                    navController.navigateSingle(EditProfileRoute)
                },
                onSettingsClick = {
                    navController.navigateSingle(SettingsRoute)
                },
            )
        }
    }
}