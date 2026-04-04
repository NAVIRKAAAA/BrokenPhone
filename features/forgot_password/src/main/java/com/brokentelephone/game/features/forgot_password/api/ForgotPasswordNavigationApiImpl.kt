package com.brokentelephone.game.features.forgot_password.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.features.forgot_password.ForgotPasswordScreen
import com.brokentelephone.game.forgot_password_api.ForgotPasswordNavigationApi
import com.brokentelephone.game.forgot_password_api.ForgotPasswordRoute
import com.brokentelephone.game.nav_api.safePopBackStack

class ForgotPasswordNavigationApiImpl : ForgotPasswordNavigationApi {

    override val route = ForgotPasswordRoute()

    override fun createRoute(email: String): ForgotPasswordRoute {
        return ForgotPasswordRoute(email)
    }

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<ForgotPasswordRoute>(
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
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ForgotPasswordRoute>()

            ForgotPasswordScreen(
                initialEmail = route.email,
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}