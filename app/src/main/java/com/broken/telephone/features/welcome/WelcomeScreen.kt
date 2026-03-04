package com.broken.telephone.features.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.broken.telephone.features.welcome.content.WelcomeContent
import com.broken.telephone.features.welcome.model.WelcomeSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    onNavigateToDashboard: () -> Unit,
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: WelcomeViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                WelcomeSideEffect.NavigateToDashboard -> onNavigateToDashboard()
            }
        }
    }

    WelcomeContent(
        onContinueAsGuest = viewModel::onContinueAsGuest,
        onGetStarted = onGetStarted,
        onSignIn = onSignIn,
        modifier = modifier,
    )
}
