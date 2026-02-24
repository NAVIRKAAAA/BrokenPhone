package com.broken.telephone.features.draw

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.R
import com.broken.telephone.core.dialog.ConfirmDialog
import com.broken.telephone.core.dialog.TimesUpDialog
import com.broken.telephone.features.draw.content.DrawContent
import com.broken.telephone.features.draw.model.DrawSideEffect
import com.broken.telephone.features.draw.model.DrawingAction
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DrawScreen(
    postId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPostSubmitted: () -> Unit = {},
    viewModel: DrawViewModel = koinViewModel { parametersOf(postId) },
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
            body = stringResource(R.string.draw_dialog_discard_body),
            cancelText = stringResource(R.string.draw_dialog_discard_cancel),
            confirmText = stringResource(R.string.common_discard),
            onDismiss = { viewModel.onDrawAction(DrawingAction.OnDiscardDismiss) },
            onConfirm = { viewModel.onDrawAction(DrawingAction.OnDiscardConfirm) },
        )
    }

    if (state.showTimesUpDialog) {
        TimesUpDialog(
            message = stringResource(R.string.draw_times_up_message),
            onGotItClick = { viewModel.onDrawAction(DrawingAction.OnTimesUpGotIt) },
        )
    }
}
