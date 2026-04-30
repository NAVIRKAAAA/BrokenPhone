package com.brokentelephone.game.main.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarRoute
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.main.activity.model.MainSideEffect
import com.brokentelephone.game.new_password_api.NewPasswordRoute
import com.brokentelephone.game.notification_details_api.NotificationDetailsRoute
import com.brokentelephone.game.sign_in_api.SignInRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HandleMainSideEffects(
    sideEffects: Flow<MainSideEffect>,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        sideEffects.collectLatest { effect ->
            when (effect) {
                is MainSideEffect.NavigateToDraw -> {
                    navController.navigate(effect.route)
                }

                is MainSideEffect.NavigateToDescribeDrawing -> {
                    navController.navigate(effect.route)
                }

                is MainSideEffect.NavigateToSignIn -> {
                    navController.navigate(WelcomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }

                    navController.navigate(SignInRoute(email = effect.email))
                }

                MainSideEffect.NavigateToNewPassword -> {
                    navController.navigate(WelcomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }

                    navController.navigate(NewPasswordRoute)
                }

                MainSideEffect.NavigateToChooseAvatar -> {
                    navController.navigate(WelcomeRoute) {
                        popUpTo(0) { inclusive = true }
                    }

                    navController.navigate(ChooseAvatarRoute)
                }

                is MainSideEffect.NavigateToNotificationDetails -> {
                    navController.navigate(NotificationDetailsRoute(effect.notificationId))
                }
            }
        }
    }
}
