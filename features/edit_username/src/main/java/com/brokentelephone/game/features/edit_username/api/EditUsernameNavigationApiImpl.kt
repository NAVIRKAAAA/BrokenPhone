package com.brokentelephone.game.features.edit_username.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.edit_username_api.EditUsernameNavigationApi
import com.brokentelephone.game.edit_username_api.EditUsernameRoute
import com.brokentelephone.game.features.edit_username.EditUsernameScreen
import com.brokentelephone.game.nav_api.safePopBackStack

class EditUsernameNavigationApiImpl : EditUsernameNavigationApi {
    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<EditUsernameRoute>(
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
            EditUsernameScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}