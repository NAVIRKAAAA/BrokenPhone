package com.brokentelephone.game.features.user_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.user_details.content.UserDetailsContent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserDetailsScreen(
    userId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserDetailsViewModel = koinViewModel { parametersOf(userId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    UserDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onTabSelect = viewModel::onTabSelect,
        modifier = modifier,
        onRefresh = viewModel::onRefresh
    )
}
