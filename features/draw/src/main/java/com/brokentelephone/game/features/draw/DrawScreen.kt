package com.brokentelephone.game.features.draw

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.core.dialog.TimesUpDialog
import com.brokentelephone.game.domain.model.session.SessionConstants
import com.brokentelephone.game.features.draw.content.DrawContent
import com.brokentelephone.game.features.draw.model.DrawSideEffect
import com.brokentelephone.game.features.draw.model.DrawingAction
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DrawScreen(
    sessionId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPostSubmitted: () -> Unit = {},
    viewModel: DrawViewModel = koinViewModel { parametersOf(sessionId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BackHandler {
        viewModel.onDrawAction(DrawingAction.OnBackClick)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is DrawSideEffect.PostCreated -> onPostSubmitted()
                DrawSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    DrawContent(
        state = state,
        onDrawAction = viewModel::onDrawAction,
        onBackClick = { viewModel.onDrawAction(DrawingAction.OnBackClick) },
        modifier = modifier
    )

    if (state.showPostConfirmDialog || state.isPosting) {
        ConfirmDialog(
            title = stringResource(R.string.draw_dialog_post_title),
            body = stringResource(R.string.draw_dialog_post_body),
            cancelText = stringResource(R.string.common_cancel),
            confirmText = stringResource(R.string.common_post),
            onDismiss = { viewModel.onDrawAction(DrawingAction.OnPostDismiss) },
            onConfirm = { viewModel.onDrawAction(DrawingAction.OnPostConfirm) },
            confirmButtonColor = MaterialTheme.colorScheme.primary,
            isLoading = state.isPosting,
        )
    }

    if (state.showDiscardDialog) {
        ConfirmDialog(
            title = stringResource(R.string.draw_dialog_discard_title),
            body = pluralStringResource(
                R.plurals.draw_dialog_discard_body,
                SessionConstants.CANCEL_COOLDOWN_MINUTES,
                SessionConstants.CANCEL_COOLDOWN_MINUTES
            ),
            cancelText = stringResource(R.string.draw_dialog_discard_cancel),
            confirmText = stringResource(R.string.common_leave),
            onDismiss = { viewModel.onDrawAction(DrawingAction.OnDiscardDismiss) },
            onConfirm = { viewModel.onDrawAction(DrawingAction.OnDiscardConfirm) },
            isLoading = state.isCancelling,
        )
    }

    if (state.showTimesUpDialog) {
        TimesUpDialog(
            message = pluralStringResource(
                R.plurals.draw_times_up_message,
                SessionConstants.CANCEL_COOLDOWN_MINUTES,
                SessionConstants.CANCEL_COOLDOWN_MINUTES
            ),
            isLoading = state.isCancelling,
            onGotItClick = { viewModel.onDrawAction(DrawingAction.OnTimesUpGotIt) },
        )
    }

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = { viewModel.onDrawAction(DrawingAction.OnGlobalErrorDismiss) },
        )
    }
}
