package com.brokentelephone.game.features.edit_avatar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.edit_avatar.content.EditAvatarContent
import com.brokentelephone.game.features.edit_avatar.model.EditAvatarEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditAvatarScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditAvatarViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditAvatarEvent.NavigateBack -> onBackClick()
            }
        }
    }

    EditAvatarContent(
        state = state,
        onBackClick = onBackClick,
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
