package com.brokentelephone.game.features.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.R
import com.brokentelephone.game.core.browser.openCustomTab
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.core.utils.isPostNotificationsGranted
import com.brokentelephone.game.features.settings.content.SettingsContent
import com.brokentelephone.game.features.settings.model.SettingsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onNavigateToWelcome: () -> Unit,
    onAccountSettingsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onBlockedUsersClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.checkNotificationsPermission(context.isPostNotificationsGranted())
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                SettingsSideEffect.NavigateToWelcome -> onNavigateToWelcome()
                is SettingsSideEffect.OpenLink -> context.openCustomTab(effect.url)
            }
        }
    }

    SettingsContent(
        state = state,
        onBackClick = onBackClick,
        onLogoutClick = viewModel::onLogoutClick,
        onAccountSettingsClick = onAccountSettingsClick,
        onNotificationsClick = onNotificationsClick,
        onLanguageClick = onLanguageClick,
        onThemeClick = onThemeClick,
        onTermsOfServiceClick = viewModel::onTermsOfServiceClick,
        onPrivacyPolicyClick = viewModel::onPrivacyPolicyClick,
        onBlockedUsersClick = onBlockedUsersClick,
        modifier = modifier,
    )

    if (state.isLogoutDialogVisible) {
        ConfirmDialog(
            title = stringResource(R.string.settings_dialog_logout_title),
            body = stringResource(R.string.settings_dialog_logout_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.settings_dialog_logout_confirm),
            onDismiss = viewModel::onLogoutDismiss,
            onConfirm = viewModel::onLogoutConfirm,
            isLoading = state.isLogoutLoading,
        )
    }

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }
}
