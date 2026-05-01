package com.brokentelephone.game.features.account_settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.dialog.ConfirmDialog
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.account_settings.content.AccountSettingsContent
import com.brokentelephone.game.features.account_settings.model.AccountSettingsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountSettingsScreen(
    onNavigateToWelcome: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: AccountSettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                AccountSettingsSideEffect.NavigateToWelcome -> onNavigateToWelcome()
            }
        }
    }

    AccountSettingsContent(
        state = state,
        onBackClick = onBackClick,
        onVerifyEmailClick = viewModel::onVerifyEmailClick,
        onDeleteAccountClick = viewModel::onDeleteAccountClick,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }

    if (state.isVerifyEmailDialogVisible) {
        ConfirmDialog(
            title = stringResource(R.string.account_settings_verify_email_dialog_title),
            body = stringResource(R.string.account_settings_verify_email_dialog_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.account_settings_verify_email_dialog_confirm),
            onDismiss = viewModel::onVerifyEmailDismissed,
            onConfirm = viewModel::onVerifyEmailConfirmed,
            isLoading = state.isVerifyEmailLoading,
        )
    }

    if (state.isDeleteAccountDialogVisible) {
        ConfirmDialog(
            title = stringResource(R.string.account_settings_dialog_delete_title),
            body = stringResource(R.string.account_settings_dialog_delete_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_delete),
            onDismiss = viewModel::onDeleteAccountDismiss,
            onConfirm = viewModel::onDeleteAccountConfirm,
            isLoading = state.isDeleteAccountLoading,
        )
    }
}
