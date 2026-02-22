package com.broken.telephone.features.account_settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.features.account_settings.content.AccountSettingsContent
import com.broken.telephone.features.account_settings.model.AccountSettingsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountSettingsScreen(
    onNavigateToWelcome: () -> Unit,
    onBlockedUsersClick: () -> Unit,
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
        onBlockedUsersClick = onBlockedUsersClick,
        onDeleteAccountClick = viewModel::onDeleteAccountClick,
        modifier = modifier,
    )

    if (state.isDeleteAccountDialogVisible) {
        ConfirmDialog(
            title = "Delete Account?",
            body = "This action cannot be undone. All your posts, contributions, and data will be permanently deleted.",
            cancelText = "Cancel",
            confirmText = "Delete",
            onDismiss = viewModel::onDeleteAccountDismiss,
            onConfirm = viewModel::onDeleteAccountConfirm,
            isLoading = state.isDeleteAccountLoading,
        )
    }
}
