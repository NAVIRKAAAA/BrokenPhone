package com.brokentelephone.game.features.welcome.api

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
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.features.sign_up_api.SignUpRoute
import com.brokentelephone.game.features.welcome.WelcomeScreen
import com.brokentelephone.game.features.welcome_api.WelcomeNavigationApi
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.sign_in_api.SignInRoute

class WelcomeNavigationApiImpl : WelcomeNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<WelcomeRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("SignUp") == true || route?.contains("SignIn") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                val route = initialState.destination.route
                if (route?.contains("SignUp") == true || route?.contains("SignIn") == true) {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = { ExitTransition.None }
        ) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigateSingle(SignUpRoute)
                },
                onSignIn = {
                    navController.navigateSingle(SignInRoute())
                },
                onNavigateToDashboard = {
                    navController.navigateSingle(DashboardRoute) {
                        popUpTo(WelcomeRoute) { inclusive = true }
                    }
                },
            )
        }
    }
}