package com.broken.telephone.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.features.settings.content.SettingsContent
import com.broken.telephone.features.settings.model.SettingsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onNavigateToWelcome: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                SettingsSideEffect.NavigateToWelcome -> onNavigateToWelcome()
            }
        }
    }

    SettingsContent(
        state = state,
        onBackClick = onBackClick,
        onLogoutClick = viewModel::onLogoutClick,
        onAccountSettingsClick = onAccountSettingsClick,
        modifier = modifier,
    )

    if (state.isLogoutDialogVisible) {
        ConfirmDialog(
            title = "Logout?",
            body = "Are you sure you want to logout?",
            cancelText = "Cancel",
            confirmText = "Logout",
            onDismiss = viewModel::onLogoutDismiss,
            onConfirm = viewModel::onLogoutConfirm,
            isLoading = state.isLogoutLoading,
        )
    }
}
