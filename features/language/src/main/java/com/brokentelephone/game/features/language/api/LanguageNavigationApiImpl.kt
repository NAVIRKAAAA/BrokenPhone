package com.brokentelephone.game.features.language.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.features.language.LanguageScreen
import com.brokentelephone.game.language_api.LanguageNavigationApi
import com.brokentelephone.game.language_api.LanguageRoute
import com.brokentelephone.game.nav_api.safePopBackStack

class LanguageNavigationApiImpl : LanguageNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<LanguageRoute>(
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
            LanguageScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}
