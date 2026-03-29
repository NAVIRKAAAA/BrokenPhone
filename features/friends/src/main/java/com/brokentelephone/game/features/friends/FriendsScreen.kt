package com.brokentelephone.game.features.friends

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
import com.brokentelephone.game.features.friends.content.FriendsContent
import com.brokentelephone.game.features.friends.model.FriendsSideEffect
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FriendsScreen(
    onBackClick: () -> Unit,
    onUserClick: (String) -> Unit,
    onAddFriendClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FriendsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                FriendsSideEffect.NavigateBack -> onBackClick()
                FriendsSideEffect.ScrollToTop -> scope.launch { listState.animateScrollToItem(0) }
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    FriendsContent(
        state = state,
        onBackClick = onBackClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onSearchClear = viewModel::onSearchClear,
        onRefresh = viewModel::onRefresh,
        onUserClick = onUserClick,
        onRemoveFriendClick = viewModel::onRemoveFriendClick,
        onAddFriendClick = onAddFriendClick,
        listState = listState,
        modifier = modifier,
    )

    if (state.selectedFriendId != null) {
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
