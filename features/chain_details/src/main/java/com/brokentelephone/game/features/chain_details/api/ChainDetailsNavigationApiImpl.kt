package com.brokentelephone.game.features.chain_details.api

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.brokentelephone.game.chain_details_api.ChainDetailsNavigationApi
import com.brokentelephone.game.chain_details_api.ChainDetailsRoute
import com.brokentelephone.game.features.chain_details.ChainDetailsScreen
import com.brokentelephone.game.features.chain_details.ChainDetailsViewModel
import com.brokentelephone.game.nav_api.safePopBackStack
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class ChainDetailsNavigationApiImpl : ChainDetailsNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<ChainDetailsRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("UserDetails") == true) {
                    EnterTransition.None
                } else {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                }
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ChainDetailsRoute>()
            val viewModel: ChainDetailsViewModel =
                koinViewModel { parametersOf(route.postId, route.userId) }
            ChainDetailsScreen(
                viewModel = viewModel,
                onBackClick = navController::safePopBackStack,
                onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                },
            )
        }
    }
}