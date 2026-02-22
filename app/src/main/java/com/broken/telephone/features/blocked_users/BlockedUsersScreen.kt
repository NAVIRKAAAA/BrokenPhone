package com.broken.telephone.features.blocked_users

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            title = "Unblock user?",
            body = "${user.name} will be able to see your posts and interact with you again.",
            cancelText = "Cancel",
            confirmText = "Unblock",
            onDismiss = viewModel::onUnblockDialogDismiss,
            onConfirm = viewModel::onUnblockConfirm,
            isLoading = state.isUnblockLoading,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
        )
    }
}
