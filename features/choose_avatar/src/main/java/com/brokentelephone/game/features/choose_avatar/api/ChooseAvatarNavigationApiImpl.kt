package com.brokentelephone.game.features.choose_avatar.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarNavigationApi
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarRoute
import com.brokentelephone.game.choose_username_api.ChooseUsernameNavigationApi
import com.brokentelephone.game.features.choose_avatar.ChooseAvatarScreen
import com.brokentelephone.game.nav_api.navigateSingle

class ChooseAvatarNavigationApiImpl(
    private val chooseUsernameNavigationApi: ChooseUsernameNavigationApi
) : ChooseAvatarNavigationApi {
    override val route = ChooseAvatarRoute

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<ChooseAvatarRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
        ) {
            ChooseAvatarScreen(
                navigateToChooseUsername = {
                    navController.navigateSingle(chooseUsernameNavigationApi.route)
                },
            )
        }
    }
}