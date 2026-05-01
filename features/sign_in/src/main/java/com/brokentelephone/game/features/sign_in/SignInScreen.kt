package com.brokentelephone.game.features.sign_in

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.core.in_app_browser.openCustomTab
import com.brokentelephone.game.features.sign_in.content.SignInContent
import com.brokentelephone.game.features.sign_in.model.SignInSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SignInScreen(
    initialEmail: String = "",
    onBackClick: () -> Unit,
    onSignedIn: () -> Unit = {},
    onNavigateToChooseAvatar: () -> Unit = {},
    onForgotPasswordClick: (email: String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel { parametersOf(initialEmail) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                SignInSideEffect.SignedIn -> onSignedIn()
                SignInSideEffect.NavigateToChooseAvatar -> onNavigateToChooseAvatar()
                SignInSideEffect.ClearFocus -> focusManager.clearFocus()
                is SignInSideEffect.OpenLink -> context.openCustomTab(effect.url)
            }
        }
    }

    SignInContent(
        state = state,
        onBackClick = onBackClick,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onSignInClick = viewModel::onSignInClick,
        onForgotPasswordClick = { onForgotPasswordClick(state.email.text) },
        onTermsClick = viewModel::onTermsClick,
        onPrivacyPolicyClick = viewModel::onPrivacyPolicyClick,
        onGoogleSignInClick = viewModel::onGoogleSignInClick,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = { viewModel.onGlobalErrorDismissed() },
        )
    }
}
