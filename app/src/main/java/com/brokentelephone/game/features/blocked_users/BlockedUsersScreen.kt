package com.brokentelephone.game.features.blocked_users

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.R
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.blocked_users.content.BlockedUsersContent
import com.brokentelephone.game.features.blocked_users.model.BlockedUsersSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BlockedUsersScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BlockedUsersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                BlockedUsersSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    BlockedUsersContent(
        state = state,
        onUnblockClick = viewModel::onUnblockClick,
        onBackClick = onBackClick,
        modifier = modifier,
        onRefresh = viewModel::onRefresh
    )

    state.loadError?.let { error ->
        ConfirmDialog(
            title = stringResource(R.string.error_session_data_title),
            body = error,
            confirmText = stringResource(R.string.error_session_data_retry),
            cancelText = stringResource(R.string.common_cancel),
            onDismiss = viewModel::onLoadErrorDismiss,
            onConfirm = viewModel::onLoadErrorRetry,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
            isLoading = state.isLoadRetrying,
        )
    }

    state.globalError?.let { error ->
        ErrorDialog(
            body = error,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }

    state.unblockDialogUser?.let { user ->
        ConfirmDialog(
            title = stringResource(R.string.blocked_users_dialog_title),
            body = stringResource(R.string.blocked_users_dialog_body, user.name),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.blocked_users_dialog_confirm),
            onDismiss = viewModel::onUnblockDialogDismiss,
            onConfirm = viewModel::onUnblockConfirm,
            isLoading = state.isUnblockLoading,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
        )
    }
}
