package com.broken.telephone.features.draw

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    if (state.showDiscardDialog) {
        ConfirmDialog(
            title = "Discard drawing?",
            body = "Your drawing will be lost if you go back.",
            cancelText = "Keep drawing",
            confirmText = "Discard",
            onDismiss = { viewModel.onDrawAction(DrawingAction.OnDiscardDismiss) },
            onConfirm = { viewModel.onDrawAction(DrawingAction.OnDiscardConfirm) },
        )
    }

    if (state.showTimesUpDialog) {
        TimesUpDialog(
            message = "Your drawing time has ended. You can try again in 10 minutes.",
            onGotItClick = { viewModel.onDrawAction(DrawingAction.OnTimesUpGotIt) },
        )
    }
}
