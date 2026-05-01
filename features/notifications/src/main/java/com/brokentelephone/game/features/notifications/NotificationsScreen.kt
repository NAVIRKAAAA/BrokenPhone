package com.brokentelephone.game.features.notifications

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.notifications.content.NotificationsContent
import com.brokentelephone.game.features.notifications.model.NotificationsSideEffect
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    onNavigateToUserDetails: (userId: String) -> Unit,
    onNavigateToChainDetails: (postId: String, userId: String) -> Unit,
    onNavigateToNotificationDetails: (notificationId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = koinViewModel(),
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
                NotificationsSideEffect.ScrollToTop -> {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                }
                is NotificationsSideEffect.NavigateToUserDetails -> {
                    onNavigateToUserDetails(effect.userId)
                }
                is NotificationsSideEffect.NavigateToChainDetails -> {
                    onNavigateToChainDetails(effect.postId, effect.userId)
                }
                is NotificationsSideEffect.NavigateToNotificationDetails -> {
                    onNavigateToNotificationDetails(effect.notificationId)
                }
            }
        }
    }

    NotificationsContent(
        state = state,
        listState = listState,
        onBackClick = onBackClick,
        onFilterSelected = viewModel::onFilterSelected,
        onNotificationClick = viewModel::onNotificationClick,
        onAcceptFriendClick = {},
        onDeclineFriendClick = {},
        onRefresh = viewModel::onRefresh,
        modifier = modifier,
    )
}
