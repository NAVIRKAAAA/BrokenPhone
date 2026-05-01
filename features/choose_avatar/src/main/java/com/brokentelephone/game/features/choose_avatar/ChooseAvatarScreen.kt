package com.brokentelephone.game.features.choose_avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.choose_avatar.content.ChooseAvatarContent
import com.brokentelephone.game.features.choose_avatar.model.ChooseAvatarEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChooseAvatarScreen(
    navigateToChooseUsername: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChooseAvatarViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is ChooseAvatarEvent.NavigateToChooseUsername -> navigateToChooseUsername()
            }
        }
    }

    ChooseAvatarContent(
        loadingAvatarId = state.loadingAvatarId,
        onAvatarClick = viewModel::onAvatarClick,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }
}
