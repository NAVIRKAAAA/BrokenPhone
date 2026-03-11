package com.brokentelephone.game.features.sign_up

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.browser.openCustomTab
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.sign_up.content.SignUpContent
import com.brokentelephone.game.features.sign_up.model.SignUpSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSignedUp: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                SignUpSideEffect.SignedUp -> onSignedUp()
                SignUpSideEffect.ClearFocus -> focusManager.clearFocus()
                is SignUpSideEffect.OpenLink -> context.openCustomTab(effect.url)
            }
        }
    }

    SignUpContent(
        state = state,
        onBackClick = onBackClick,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
        onSignUpClick = viewModel::onSignUpClick,
        onSignInClick = onSignInClick,
        onTermsClick = viewModel::onTermsClick,
        onPrivacyPolicyClick = viewModel::onPrivacyPolicyClick,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = { viewModel.onGlobalErrorDismissed() },
        )
    }
}
