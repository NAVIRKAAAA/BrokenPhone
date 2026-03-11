package com.brokentelephone.game.features.choose_username

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.brokentelephone.game.features.choose_username.content.ChooseUsernameContent
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChooseUsernameScreen(
    onBackClick: () -> Unit,
    onUsernameSelected: (username: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChooseUsernameViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ChooseUsernameEvent.NavigateNext -> onUsernameSelected(event.username)
                ChooseUsernameEvent.NavigateBack -> onBackClick()
            }
        }
    }

    ChooseUsernameContent(
        state = state,
        onBackClick = viewModel::onBack,
        onUsernameChange = viewModel::onUsernameChange,
        onContinueClick = viewModel::onContinueClick,
        modifier = modifier,
    )
}
