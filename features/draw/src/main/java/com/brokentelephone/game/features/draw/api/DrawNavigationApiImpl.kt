package com.brokentelephone.game.features.draw.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.draw_api.DrawNavigationApi
import com.brokentelephone.game.draw_api.DrawRoute
import com.brokentelephone.game.features.draw.DrawScreen
import com.brokentelephone.game.nav_api.KEY_FORCE_REFRESH
import com.brokentelephone.game.nav_api.safePopBackStack

class DrawNavigationApiImpl : DrawNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<DrawRoute>(
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
            val route = backStackEntry.toRoute<DrawRoute>()
            DrawScreen(
                sessionId = route.sessionId,
                onBackClick = navController::safePopBackStack,
                onPostSubmitted = {
                    runCatching {
                        navController.getBackStackEntry(DashboardRoute)
                            .savedStateHandle[KEY_FORCE_REFRESH] = true
                    }

                    navController.popBackStack(DashboardRoute, inclusive = false)
                }
            )
        }
    }

}