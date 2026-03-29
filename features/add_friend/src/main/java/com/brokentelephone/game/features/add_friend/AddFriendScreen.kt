package com.brokentelephone.game.features.add_friend

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.add_friend.content.AddFriendContent
import com.brokentelephone.game.features.add_friend.model.AddFriendSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AddFriendScreen(
    onBackClick: () -> Unit,
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
        onAddFriendClick = {},
        onCancelRequestClick = {},
        modifier = modifier,
        onRefresh = viewModel::onRefresh,
        listState = listState
    )

    state.globalError?.let { error ->
        ErrorDialog(
            body = error,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }
}
