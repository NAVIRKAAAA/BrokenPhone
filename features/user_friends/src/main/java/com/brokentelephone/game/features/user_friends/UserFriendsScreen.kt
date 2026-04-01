package com.brokentelephone.game.features.user_friends

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.user_friends.content.UserFriendsContent
import com.brokentelephone.game.features.user_friends.model.UserFriendsSideEffect
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserFriendsScreen(
    userId: String,
    onBackClick: () -> Unit,
    onUserClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserFriendsViewModel = koinViewModel { parametersOf(userId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                UserFriendsSideEffect.NavigateBack -> onBackClick()
                UserFriendsSideEffect.ScrollToTop -> scope.launch { listState.animateScrollToItem(0) }
            }
        }
    }

    UserFriendsContent(
        state = state,
        listState = listState,
        onBackClick = onBackClick,
        modifier = modifier,
        onRefresh = viewModel::onRefresh,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSearchClear = viewModel::onSearchClear,
        onAddFriendClick = viewModel::onAddFriendClick,
        onRemoveFriendClick = viewModel::onRemoveFriendClick,
        onCancelRequestClick = viewModel::onCancelRequestClick,
        onAcceptRequestClick = viewModel::onAcceptRequestClick,
        onUserClick = onUserClick,
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
