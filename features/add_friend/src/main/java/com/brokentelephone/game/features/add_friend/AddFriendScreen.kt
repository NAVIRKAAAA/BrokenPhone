package com.brokentelephone.game.features.add_friend

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.add_friend.content.AddFriendContent
import com.brokentelephone.game.features.add_friend.model.AddFriendSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddFriendScreen(
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddFriendViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { effect ->
            when (effect) {
                AddFriendSideEffect.ScrollToTop -> {
                    listState.animateScrollToItem(0)
                }
            }
        }
    }

    AddFriendContent(
        state = state,
        onBackClick = onBackClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSearchClear = viewModel::onSearchClear,
        onUserClick = onUserClick,
        onAddFriendClick = viewModel::onAddFriendClick,
        onCancelRequestClick = viewModel::onCancelRequestClick,
        onRemoveFriendClick = viewModel::onRemoveFriendClick,
        onAcceptRequestClick = viewModel::onAcceptRequestClick,
        onDeclineRequestClick = viewModel::onDeclineRequestClick,
        modifier = modifier,
        onRefresh = viewModel::onRefresh,
        listState = listState
    )

    if (state.cancelRequestDialogUserId != null) {
        ConfirmDialog(
            title = stringResource(R.string.common_dialog_cancel_request_title),
            body = stringResource(R.string.common_dialog_cancel_request_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_dialog_cancel_request_confirm),
            onDismiss = viewModel::onCancelRequestDialogDismiss,
            onConfirm = viewModel::onCancelRequestConfirm,
            isLoading = state.isCancelRequestLoading,
        )
    }

    if (state.removeFriendDialogUserId != null) {
        ConfirmDialog(
            title = stringResource(R.string.common_dialog_remove_friend_title),
            body = stringResource(R.string.common_dialog_remove_friend_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_dialog_remove_friend_confirm),
            onDismiss = viewModel::onRemoveFriendDialogDismiss,
            onConfirm = viewModel::onRemoveFriendConfirm,
            isLoading = state.isRemoveFriendLoading,
        )
    }

    state.globalError?.let { error ->
        ErrorDialog(
            body = error,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }
}
