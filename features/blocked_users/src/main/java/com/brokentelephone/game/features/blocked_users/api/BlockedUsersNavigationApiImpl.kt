package com.brokentelephone.game.features.blocked_users.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.blocked_users_api.BlockedUsersNavigationApi
import com.brokentelephone.game.blocked_users_api.BlockedUsersRoute
import com.brokentelephone.game.features.blocked_users.BlockedUsersScreen
import com.brokentelephone.game.nav_api.safePopBackStack

class BlockedUsersNavigationApiImpl : BlockedUsersNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<BlockedUsersRoute>(
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
            BlockedUsersScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}
