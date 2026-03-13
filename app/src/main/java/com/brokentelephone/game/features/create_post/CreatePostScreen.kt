package com.brokentelephone.game.features.create_post

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.R
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.create_post.content.CreatePostContent
import com.brokentelephone.game.features.create_post.dialog.chain_settings.ChainSettingsDialog
import com.brokentelephone.game.features.create_post.dialog.start_new_chain.StartNewChainDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPostCreated: () -> Unit = {},
    viewModel: CreatePostViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BackHandler {
        viewModel.onBackClick()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is CreatePostSideEffect.PostCreated -> onPostCreated()
                CreatePostSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    CreatePostContent(
        state = state,
        onTextChanged = viewModel::onTextChanged,
        onBadgeClick = viewModel::onShowChainSettings,
        onPostClick = viewModel::onShowStartNewChain,
        onBackClick = viewModel::onBackClick,
        modifier = modifier
    )

    state.globalError?.let { message ->
        ErrorDialog(
            body = message,
            onOkClick = viewModel::onGlobalErrorDismissed,
        )
    }

    if (state.showDiscardDialog) {
        ConfirmDialog(
            title = stringResource(R.string.create_post_dialog_discard_title),
            body = stringResource(R.string.create_post_dialog_discard_body),
            cancelText = stringResource(R.string.common_keep_writing),
            confirmText = stringResource(R.string.common_discard),
            onDismiss = viewModel::onDiscardDismiss,
            onConfirm = viewModel::onDiscardConfirm,
        )
    }

    if (state.showStartNewChain) {
        StartNewChainDialog(
            maxGenerations = state.maxGenerations,
            textTimeLimit = state.textTimeLimit,
            drawingTimeLimit = state.drawingTimeLimit,
            isLoading = state.isLoading,
            onDismiss = viewModel::onDismissStartNewChain,
            onCancel = viewModel::onDismissStartNewChain,
            onStartChain = viewModel::onStartChain,
        )
    }

    if (state.showChainSettings) {
        ChainSettingsDialog(
            initialChainLength = state.maxGenerations,
            initialTextTimeLimit = state.textTimeLimit,
            initialDrawingTimeLimit = state.drawingTimeLimit,
            onDismiss = viewModel::onDismissChainSettings,
            onConfirm = viewModel::onChainSettingsConfirmed,
        )
    }

}
