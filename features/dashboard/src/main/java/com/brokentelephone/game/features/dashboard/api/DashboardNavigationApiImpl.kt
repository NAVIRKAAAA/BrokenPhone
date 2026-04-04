package com.brokentelephone.game.features.dashboard.api

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.dashboard_api.DashboardNavigationApi
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.features.dashboard.DashboardScreen
import com.brokentelephone.game.features.dashboard.DashboardViewModel
import com.brokentelephone.game.nav_api.KEY_FORCE_REFRESH
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.post_details_api.PostDetailsRoute
import org.koin.compose.viewmodel.koinViewModel

class DashboardNavigationApiImpl : DashboardNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<DashboardRoute>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {

            val viewModel: DashboardViewModel = koinViewModel()
            val savedStateHandle = it.savedStateHandle

            LaunchedEffect(Unit) {
                val isForceRefresh = savedStateHandle.remove<Boolean>(KEY_FORCE_REFRESH) == true

                if (isForceRefresh) {
                    viewModel.onRefresh()
                }
            }

            DashboardScreen(
                viewModel = viewModel,
                onPostClick = { postId ->
                    navController.navigateSingle(PostDetailsRoute(postId = postId))
                },
                onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                },
                onNotificationsClick = {
//                        navController.navigateSingle(Routes.Notifications)
                }
            )
        }
    }
}