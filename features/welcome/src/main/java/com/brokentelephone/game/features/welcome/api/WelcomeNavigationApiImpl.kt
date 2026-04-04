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
import com.brokentelephone.game.features.sign_up_api.SignUpNavigationApi
import com.brokentelephone.game.features.welcome.WelcomeScreen
import com.brokentelephone.game.features.welcome_api.WelcomeNavigationApi
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.sign_in_api.SignInNavigationApi

class WelcomeNavigationApiImpl(
    private val signUpNavigationApi: SignUpNavigationApi,
    private val signInNavigationApi: SignInNavigationApi
) : WelcomeNavigationApi {

    override val route = WelcomeRoute

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
                    navController.navigateSingle(signUpNavigationApi.route)
                },
                onSignIn = {
                    navController.navigateSingle(signInNavigationApi.route)
                },
                onNavigateToDashboard = { },
            )
        }
    }
}