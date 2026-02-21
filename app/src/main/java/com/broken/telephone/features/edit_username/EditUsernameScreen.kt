package com.broken.telephone.features.edit_username

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.edit_username.content.EditUsernameContent
import com.broken.telephone.features.edit_username.model.EditUsernameEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditUsernameScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditUsernameViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditUsernameEvent.NavigateBack -> onBackClick()
            }
        }
    }

    EditUsernameContent(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSave,
        onUsernameChange = viewModel::onUsernameChange,
        modifier = modifier,
    )
}
