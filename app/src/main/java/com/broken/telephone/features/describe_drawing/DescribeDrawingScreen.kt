package com.broken.telephone.features.describe_drawing

import androidx.activity.compose.BackHandler
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            title = "Post description?",
            body = "Your description will be submitted and visible to others.",
            cancelText = "Cancel",
            confirmText = "Post",
            onDismiss = viewModel::onPostDismiss,
            onConfirm = viewModel::onPostConfirm,
            confirmButtonColor = MaterialTheme.colorScheme.primary,
            isLoading = state.isPosting,
        )
    }

    if (state.showDiscardDialog) {
        ConfirmDialog(
            title = "Discard description?",
            body = "Your text will be lost if you go back.",
            cancelText = "Keep writing",
            confirmText = "Discard",
            onDismiss = viewModel::onDiscardDismiss,
            onConfirm = viewModel::onDiscardConfirm,
        )
    }

    if (state.showTimesUpDialog) {
        TimesUpDialog(
            message = "Your writing time has ended. You can try again in 10 minutes.",
            onGotItClick = viewModel::onTimesUpGotIt,
        )
    }
}
