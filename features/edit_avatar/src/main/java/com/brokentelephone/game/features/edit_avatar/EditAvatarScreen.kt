package com.brokentelephone.game.features.edit_avatar

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.edit_avatar.content.EditAvatarContent
import com.brokentelephone.game.features.edit_avatar.model.EditAvatarEvent
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditAvatarScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditAvatarViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditAvatarEvent.ScrollToTop -> {
                    scope.launch {
                        gridState.scrollToItem(0)
                    }
                }
            }
        }
    }

    EditAvatarContent(
        state = state,
        onBackClick = onBackClick,
        onAvatarClick = viewModel::onAvatarClick,
        modifier = modifier,
        gridState = gridState,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }
}
