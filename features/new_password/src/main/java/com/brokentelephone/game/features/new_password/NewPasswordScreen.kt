package com.brokentelephone.game.features.new_password

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.new_password.content.NewPasswordContent
import com.brokentelephone.game.features.new_password.model.NewPasswordSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewPasswordScreen(
    onBackClick: () -> Unit,
    onPasswordUpdated: (email: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                NewPasswordSideEffect.NavigateBack -> onBackClick()
                is NewPasswordSideEffect.NavigateToSignIn -> onPasswordUpdated(effect.email)
            }
        }
    }

    NewPasswordContent(
        state = state,
        onBackClick = onBackClick,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onTogglePasswordVisibility = viewModel::onTogglePasswordVisibility,
        onToggleConfirmPasswordVisibility = viewModel::onToggleConfirmPasswordVisibility,
        onSaveClick = viewModel::onSaveClick,
        modifier = modifier,
    )
}
