package com.broken.telephone.features.notifications

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.notifications.content.NotificationsContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: NotificationsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NotificationsContent(
        state = state,
        onBackClick = onBackClick,
        onAllNotificationsToggle = viewModel::onAllNotificationsToggle,
        onNotificationToggle = viewModel::onNotificationToggle,
        modifier = modifier,
    )
}
