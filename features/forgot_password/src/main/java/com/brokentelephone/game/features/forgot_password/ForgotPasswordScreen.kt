package com.brokentelephone.game.features.forgot_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.forgot_password.content.ForgotPasswordContent
import com.brokentelephone.game.features.forgot_password.content.PasswordResetSentDialog
import com.brokentelephone.game.features.forgot_password.model.ForgotPasswordSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ForgotPasswordScreen(
    initialEmail: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = koinViewModel { parametersOf(initialEmail) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                ForgotPasswordSideEffect.ClearFocus -> focusManager.clearFocus()
                ForgotPasswordSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    ForgotPasswordContent(
        state = state,
        onBackClick = onBackClick,
        onEmailChanged = viewModel::onEmailChanged,
        onSendClick = viewModel::onSendClick,
        modifier = modifier,
    )

    if (state.isResetLinkSent) {
        PasswordResetSentDialog(
            onGotItClick = { viewModel.onResetLinkSentDismissed() },
        )
    }

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = {
                viewModel.onGlobalErrorDismissed()
            },
        )
    }
}
