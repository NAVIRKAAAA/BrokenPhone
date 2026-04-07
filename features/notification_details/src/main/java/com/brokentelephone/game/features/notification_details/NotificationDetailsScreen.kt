package com.brokentelephone.game.features.notification_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.notification_details.content.NotificationDetailsContent
import com.brokentelephone.game.features.notification_details.model.NotificationDetailsSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun NotificationDetailsScreen(
    notificationId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotificationDetailsViewModel = koinViewModel { parametersOf(notificationId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                NotificationDetailsSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    NotificationDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onMoreClick = {},
        modifier = modifier,
    )
}
