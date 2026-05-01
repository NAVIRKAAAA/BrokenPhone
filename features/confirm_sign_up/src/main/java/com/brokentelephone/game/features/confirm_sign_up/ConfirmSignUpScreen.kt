package com.brokentelephone.game.features.confirm_sign_up

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.confirm_sign_up.content.ConfirmSignUpContent
import com.brokentelephone.game.features.confirm_sign_up.model.ConfirmSignUpSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ConfirmSignUpScreen(
    email: String,
    onBackClick: () -> Unit,
    onEmailVerified: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ConfirmSignUpViewModel = koinViewModel(parameters = { parametersOf(email) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                ConfirmSignUpSideEffect.EmailVerified -> onEmailVerified()
            }
        }
    }

    ConfirmSignUpContent(
        state = state,
        onBackClick = onBackClick,
        onCodeChange = viewModel::onCodeChange,
        onConfirmClick = viewModel::onConfirmClick,
        onResendClick = viewModel::onResendClick,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }
}
