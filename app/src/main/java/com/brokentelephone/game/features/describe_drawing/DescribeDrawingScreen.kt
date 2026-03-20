package com.brokentelephone.game.features.describe_drawing

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.R
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.core.dialog.TimesUpDialog
import com.brokentelephone.game.domain.model.session.SessionConstants
import com.brokentelephone.game.features.describe_drawing.content.DescribeDrawingContent
import com.brokentelephone.game.features.describe_drawing.model.DescribeDrawingSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DescribeDrawingScreen(
    sessionId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPostSubmitted: () -> Unit = {},
    viewModel: DescribeDrawingViewModel = koinViewModel { parametersOf(sessionId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                DescribeDrawingSideEffect.PostCreated -> onPostSubmitted()
                DescribeDrawingSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    DescribeDrawingContent(
        state = state,
        onBackClick = viewModel::onBackClick,
        onTextChanged = viewModel::onTextChanged,
        onPostClick = viewModel::onPostClick,
        modifier = modifier
    )

    if (state.showPostConfirmDialog || state.isPosting) {
        ConfirmDialog(
            title = stringResource(R.string.describe_drawing_dialog_post_title),
            body = stringResource(R.string.describe_drawing_dialog_post_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_post),
            onDismiss = viewModel::onPostDismiss,
            onConfirm = viewModel::onPostConfirm,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
            isLoading = state.isPosting,
        )
    }

    if (state.showDiscardDialog) {
        ConfirmDialog(
            title = stringResource(R.string.describe_drawing_dialog_discard_title),
            body = pluralStringResource(
                R.plurals.describe_drawing_dialog_discard_body,
                SessionConstants.CANCEL_COOLDOWN_MINUTES,
                SessionConstants.CANCEL_COOLDOWN_MINUTES
            ),
            cancelText = stringResource(R.string.common_keep_writing),
            confirmText = stringResource(R.string.common_leave),
            onDismiss = { if (!state.isCancelling) viewModel.onDiscardDismiss() },
            onConfirm = viewModel::onDiscardConfirm,
            isLoading = state.isCancelling,
        )
    }

    if (state.showTimesUpDialog) {
        TimesUpDialog(
            message = pluralStringResource(
                R.plurals.describe_drawing_times_up_message,
                SessionConstants.CANCEL_COOLDOWN_MINUTES,
                SessionConstants.CANCEL_COOLDOWN_MINUTES
            ),
            isLoading = state.isCancelling,
            onGotItClick = viewModel::onTimesUpGotIt,
        )
    }

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }
}
