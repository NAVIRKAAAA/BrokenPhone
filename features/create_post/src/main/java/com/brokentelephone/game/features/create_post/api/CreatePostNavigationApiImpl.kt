package com.brokentelephone.game.features.create_post.api

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.brokentelephone.game.create_post_api.CreatePostNavigationApi
import com.brokentelephone.game.create_post_api.CreatePostRoute
import com.brokentelephone.game.features.create_post.CreatePostScreen
import com.brokentelephone.game.nav_api.safePopBackStack

class CreatePostNavigationApiImpl : CreatePostNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {
        builder.composable<CreatePostRoute>(
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) {
            CreatePostScreen(
                onBackClick = navController::safePopBackStack,
                onPostCreated = navController::safePopBackStack
            )
        }
    }
}
