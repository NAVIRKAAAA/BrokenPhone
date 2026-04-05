package com.brokentelephone.game.features.post_details.api

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
import com.brokentelephone.game.chain_details_api.ChainDetailsRoute
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.describe_drawing_api.DescribeDrawingRoute
import com.brokentelephone.game.draw_api.DrawRoute
import com.brokentelephone.game.features.post_details.PostDetailsScreen
import com.brokentelephone.game.features.post_details.PostDetailsViewModel
import com.brokentelephone.game.nav_api.KEY_FORCE_REFRESH
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.post_details_api.PostDetailsNavigationApi
import com.brokentelephone.game.post_details_api.PostDetailsRoute
import com.brokentelephone.game.user_details_api.UserDetailsRoute
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class PostDetailsNavigationApiImpl : PostDetailsNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<PostDetailsRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("Draw") == true || route?.contains("ChainDetails") == true) {
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
                if (route?.contains("Draw") == true || route?.contains("ChainDetails") == true) {
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
            val route = backStackEntry.toRoute<PostDetailsRoute>()
            val viewModel: PostDetailsViewModel = koinViewModel { parametersOf(route.postId) }
            PostDetailsScreen(
                viewModel = viewModel,
                onBackClick = navController::safePopBackStack,
                navigateBackWithForceUpdate = {
                    navController.getBackStackEntry(DashboardRoute)
                        .savedStateHandle[KEY_FORCE_REFRESH] = true

                    navController.safePopBackStack()
                },
                onDrawContinue = { sessionId ->
                        navController.navigateSingle(DrawRoute(sessionId = sessionId))
//                    onBannerDismissed()
                },
                onDescribeDrawingContinue = { sessionId ->
                        navController.navigateSingle(DescribeDrawingRoute(sessionId = sessionId))
//                    onBannerDismissed()
                },
                onViewHistoryClick = { postId ->
                        navController.navigateSingle(ChainDetailsRoute(postId = postId))
                },
                onUserClick = { userId ->
                        navController.navigateSingle(UserDetailsRoute(userId = userId))
                },
            )
        }
    }
}