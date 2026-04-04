package com.brokentelephone.game.features.sign_in.api

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
import androidx.navigation.toRoute
import com.brokentelephone.game.features.sign_in.SignInScreen
import com.brokentelephone.game.forgot_password_api.ForgotPasswordNavigationApi
import com.brokentelephone.game.nav_api.NavigationRoute
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.sign_in_api.SignInNavigationApi
import com.brokentelephone.game.sign_in_api.SignInRoute

class SignInNavigationApiImpl(
    private val forgotPasswordNavigationApi: ForgotPasswordNavigationApi
) : SignInNavigationApi {

    override val route: NavigationRoute = SignInRoute()

    override fun createRoute(email: String): SignInRoute {
        return SignInRoute(email)
    }

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {

        builder.composable<SignInRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("SignUp") == true || route?.contains("ForgotPassword") == true) {
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
                if (route?.contains("SignUp") == true || route?.contains("ForgotPassword") == true) {
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
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<SignInRoute>()

            SignInScreen(
                initialEmail = route.email,
                onBackClick = navController::safePopBackStack,
                onSignedIn = {
//                        navController.navigateSingle(Routes.Dashboard) {
//                            popUpTo(0) { inclusive = true }
//                        }
                },
                onNavigateToChooseAvatar = {
//                        navController.navigateSingle(Routes.ChooseAvatar) {
//                            popUpTo(0) { inclusive = true }
//                        }
                },
                onForgotPasswordClick = { email ->
                    val route = forgotPasswordNavigationApi.createRoute(email)
                    navController.navigateSingle(route)
                },
            )
        }
    }
}