package com.broken.telephone.features.describe_drawing

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
import com.broken.telephone.features.describe_drawing.content.DescribeDrawingContent
import com.broken.telephone.features.describe_drawing.model.DescribeDrawingSideEffect
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun DescribeDrawingScreen(
    postId: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPostSubmitted: () -> Unit = {},
    viewModel: DescribeDrawingViewModel = koinViewModel { parametersOf(postId) },
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
            body = stringResource(R.string.describe_drawing_dialog_discard_body),
            cancelText = stringResource(R.string.common_keep_writing),
            confirmText = stringResource(R.string.common_discard),
            onDismiss = viewModel::onDiscardDismiss,
            onConfirm = viewModel::onDiscardConfirm,
        )
    }

    if (state.showTimesUpDialog) {
        TimesUpDialog(
            message = stringResource(R.string.describe_drawing_times_up_message),
            onGotItClick = viewModel::onTimesUpGotIt,
        )
    }
}
