package com.broken.telephone.features.blocked_users

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.R
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.features.blocked_users.content.BlockedUsersContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BlockedUsersScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BlockedUsersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BlockedUsersContent(
        state = state,
        onUnblockClick = viewModel::onUnblockClick,
        onBackClick = onBackClick,
        modifier = modifier,
    )

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
