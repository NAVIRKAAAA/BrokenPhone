package com.broken.telephone.features.sign_in

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.sign_in.content.SignInContent
import com.broken.telephone.features.sign_in.model.SignInSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onBackClick: () -> Unit,
    onSignedIn: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                SignInSideEffect.SignedIn -> onSignedIn()
                SignInSideEffect.ClearFocus -> focusManager.clearFocus()
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
        onSignUpClick = onSignUpClick,
        modifier = modifier,
    )
}
