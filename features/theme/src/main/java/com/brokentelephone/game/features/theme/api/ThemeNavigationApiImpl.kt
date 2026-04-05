package com.brokentelephone.game.features.theme.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.features.theme.ThemeScreen
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.theme_api.ThemeNavigationApi
import com.brokentelephone.game.theme_api.ThemeRoute

class ThemeNavigationApiImpl : ThemeNavigationApi {

    override fun screen(navController: NavController, builder: NavGraphBuilder) {
        builder.composable<ThemeRoute>(
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
            ThemeScreen(
                onBackClick = navController::safePopBackStack,
            )
        }
    }
}
