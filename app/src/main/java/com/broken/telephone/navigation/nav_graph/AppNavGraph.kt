package com.broken.telephone.navigation.nav_graph

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.broken.telephone.features.create_post.CreatePostScreen
import com.broken.telephone.features.dashboard.DashboardScreen
import com.broken.telephone.features.welcome.WelcomeScreen
import com.broken.telephone.navigation.routes.Routes
import com.broken.telephone.navigation.utils.navigateSingle
import com.broken.telephone.navigation.utils.safePopBackStack

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Welcome,
        modifier = modifier,
    ) {
        composable<Routes.Welcome>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            WelcomeScreen(
                onContinueAsGuest = {
                    navController.navigateSingle(Routes.Dashboard) {
                        popUpTo(Routes.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.Dashboard>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            DashboardScreen()
        }

        composable<Routes.CreatePost>(
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(500)
                )
            },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(800)
                ) + fadeOut(
                    animationSpec = tween(500)
                )
            }
        ) {
            CreatePostScreen(
                onBackClick = navController::safePopBackStack
            )
        }
    }
}