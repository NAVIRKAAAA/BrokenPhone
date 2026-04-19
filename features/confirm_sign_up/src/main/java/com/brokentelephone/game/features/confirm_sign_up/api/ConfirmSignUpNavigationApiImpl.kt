package com.brokentelephone.game.features.confirm_sign_up.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarRoute
import com.brokentelephone.game.features.confirm_sign_up.ConfirmSignUpScreen
import com.brokentelephone.game.features.confirm_sign_up_api.ConfirmSignUpNavigationApi
import com.brokentelephone.game.features.confirm_sign_up_api.ConfirmSignUpRoute
import com.brokentelephone.game.nav_api.safePopBackStack

class ConfirmSignUpNavigationApiImpl : ConfirmSignUpNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<ConfirmSignUpRoute>(
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
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ConfirmSignUpRoute>()
            ConfirmSignUpScreen(
                email = route.email,
                onBackClick = navController::safePopBackStack,
                onEmailVerified = {
                    navController.navigate(ChooseAvatarRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
