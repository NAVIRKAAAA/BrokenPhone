package com.brokentelephone.game.features.sign_up.api

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
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarRoute
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.features.confirm_sign_up_api.ConfirmSignUpRoute
import com.brokentelephone.game.features.sign_up.SignUpScreen
import com.brokentelephone.game.features.sign_up_api.SignUpNavigationApi
import com.brokentelephone.game.features.sign_up_api.SignUpRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack

class SignUpNavigationApiImpl : SignUpNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<SignUpRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                if (targetState.destination.route?.contains("SignIn") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("SignIn") == true) {
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
            SignUpScreen(
                onBackClick = navController::safePopBackStack,
                onSignedUp = {
                    navController.navigateSingle(DashboardRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToChooseAvatar = {
                    navController.navigate(ChooseAvatarRoute) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToConfirmSignUp = { email ->
                    // Auto navigate. Don't use navigateSingle !
                    navController.navigate(ConfirmSignUpRoute(email = email))
                },
            )
        }
    }
}
