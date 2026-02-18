package com.broken.telephone.features.create_post

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.create_post.dialog.chain_settings.ChainSettingsDialog
import com.broken.telephone.features.create_post.content.CreatePostContent
import com.broken.telephone.features.create_post.dialog.start_new_chain.StartNewChainDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: CreatePostViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    CreatePostContent(
        state = state,
        onTextChanged = viewModel::onTextChanged,
        onBadgeClick = viewModel::onShowChainSettings,
        onPostClick = viewModel::onShowStartNewChain,
        onBackClick = onBackClick,
        modifier = modifier
    )

    if (state.showStartNewChain) {
        StartNewChainDialog(
            maxGenerations = state.maxGenerations,
            textTimeLimit = state.textTimeLimit,
            drawingTimeLimit = state.drawingTimeLimit,
            onDismiss = viewModel::onDismissStartNewChain,
            onCancel = viewModel::onDismissStartNewChain,
            onStartChain = viewModel::onDismissStartNewChain,
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