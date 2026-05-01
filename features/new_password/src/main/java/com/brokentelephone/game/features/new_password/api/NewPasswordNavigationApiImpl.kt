package com.brokentelephone.game.features.new_password.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.features.new_password.NewPasswordScreen
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.new_password_api.NewPasswordNavigationApi
import com.brokentelephone.game.new_password_api.NewPasswordRoute
import com.brokentelephone.game.sign_in_api.SignInRoute

class NewPasswordNavigationApiImpl : NewPasswordNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder,
    ) {
        builder.composable<NewPasswordRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) {
            NewPasswordScreen(
                onBackClick = navController::safePopBackStack,
                onPasswordUpdated = { email ->
                    navController.navigate(SignInRoute(email = email)) {
                        popUpTo<WelcomeRoute> { inclusive = false }
                    }
                },
            )
        }
    }
}
