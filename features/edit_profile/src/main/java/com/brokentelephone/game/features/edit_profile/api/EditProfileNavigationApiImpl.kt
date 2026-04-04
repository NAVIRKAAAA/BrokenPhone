package com.brokentelephone.game.features.edit_profile.api

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
import com.brokentelephone.game.edit_avatar_api.EditAvatarRoute
import com.brokentelephone.game.edit_bio_api.EditBioRoute
import com.brokentelephone.game.edit_email_api.EditEmailRoute
import com.brokentelephone.game.edit_profile_api.EditProfileNavigationApi
import com.brokentelephone.game.edit_profile_api.EditProfileRoute
import com.brokentelephone.game.edit_username_api.EditUsernameRoute
import com.brokentelephone.game.features.edit_profile.EditProfileScreen
import com.brokentelephone.game.nav_api.navigateSingle
import com.brokentelephone.game.nav_api.safePopBackStack

class EditProfileNavigationApiImpl : EditProfileNavigationApi {

    override fun screen(
        navController: NavController,
        builder: NavGraphBuilder
    ) {

        builder.composable<EditProfileRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("EditUsername") == true || route?.contains("EditAvatar") == true || route?.contains(
                        "EditEmail"
                    ) == true || route?.contains("EditBio") == true
                ) {
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
                if (route?.contains("EditUsername") == true || route?.contains("EditAvatar") == true || route?.contains(
                        "EditEmail"
                    ) == true || route?.contains("EditBio") == true
                ) {
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
        ) {
            EditProfileScreen(
                onBackClick = navController::safePopBackStack,
                onEditPhotoClick = {
                    navController.navigateSingle(EditAvatarRoute)
                },
                onEditUsernameClick = {
                    navController.navigateSingle(EditUsernameRoute)
                },
                onEditBioClick = {
                    navController.navigateSingle(EditBioRoute)
                },
                onEditEmailClick = {
                    navController.navigateSingle(EditEmailRoute)
                },
            )
        }

    }
}