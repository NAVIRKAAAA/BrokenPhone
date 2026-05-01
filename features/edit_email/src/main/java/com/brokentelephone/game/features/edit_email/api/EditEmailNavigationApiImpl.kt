package com.brokentelephone.game.features.edit_email.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.edit_email_api.EditEmailNavigationApi
import com.brokentelephone.game.edit_email_api.EditEmailRoute
import com.brokentelephone.game.features.edit_email.EditEmailScreen
import com.brokentelephone.game.nav_api.safePopBackStack

class EditEmailNavigationApiImpl : EditEmailNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<EditEmailRoute>(
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
            EditEmailScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }

}