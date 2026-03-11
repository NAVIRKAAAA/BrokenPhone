package com.brokentelephone.game.features.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.welcome.content.WelcomeContent
import com.brokentelephone.game.features.welcome.model.WelcomeSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier,
    onGetStarted: () -> Unit = {},
    onSignIn: () -> Unit = {},
    viewModel: WelcomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                WelcomeSideEffect.NavigateToDashboard -> onNavigateToDashboard()
            }
        }
    }

    WelcomeContent(
        state = state,
        onPlayAsGuestClick = viewModel::onPlayAsGuestClick,
        onGetStarted = onGetStarted,
        onSignIn = onSignIn,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = { viewModel.onGlobalErrorDismissed() },
        )
    }
}
