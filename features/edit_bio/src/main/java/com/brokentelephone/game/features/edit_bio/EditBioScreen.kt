package com.brokentelephone.game.features.edit_bio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.edit_bio.content.EditBioContent
import com.brokentelephone.game.features.edit_bio.model.EditBioEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditBioScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditBioViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditBioEvent.NavigateBack -> onBackClick()
            }
        }
    }

    EditBioContent(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSave,
        onBioChange = viewModel::onBioChange,
        modifier = modifier,
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }
}
