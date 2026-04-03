package com.brokentelephone.game.features.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.notifications.content.NotificationsContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    NotificationsContent(
        state = state,
        onBackClick = onBackClick,
        onFilterSelected = {},
        onNotificationClick = {},
        onAcceptFriendClick = {},
        onDeclineFriendClick = {},
        onRefresh = viewModel::onRefresh,
        modifier = modifier,
    )
}
