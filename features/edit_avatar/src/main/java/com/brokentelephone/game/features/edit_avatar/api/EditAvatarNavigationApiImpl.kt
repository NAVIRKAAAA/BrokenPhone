package com.brokentelephone.game.features.edit_avatar.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.edit_avatar_api.EditAvatarNavigationApi
import com.brokentelephone.game.edit_avatar_api.EditAvatarRoute
import com.brokentelephone.game.features.edit_avatar.EditAvatarScreen
import com.brokentelephone.game.nav_api.safePopBackStack

class EditAvatarNavigationApiImpl : EditAvatarNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<EditAvatarRoute>(
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
            EditAvatarScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }

}