package com.brokentelephone.game.features.edit_email

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.edit_email.content.EditEmailContent
import com.brokentelephone.game.features.edit_email.model.EditEmailEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditEmailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditEmailViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditEmailEvent.NavigateBack -> onBackClick()
            }
        }
    }

    EditEmailContent(
        state = state,
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSave,
        onEmailChange = viewModel::onEmailChange,
        modifier = modifier,
    )

    if (state.showConfirmDialog) {
        ConfirmDialog(
            title = stringResource(R.string.edit_email_confirm_title),
            body = stringResource(R.string.edit_email_confirm_body),
            cancelText = stringResource(R.string.edit_email_confirm_cancel),
            confirmText = stringResource(R.string.edit_email_confirm_ok),
            onDismiss = viewModel::onSaveDialogDismissed,
            onConfirm = viewModel::onSaveConfirmed,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
            isLoading = state.isLoading,
        )
    }

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }
}
