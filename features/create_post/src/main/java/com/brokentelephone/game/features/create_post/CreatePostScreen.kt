package com.brokentelephone.game.features.create_post

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.composable.dialog.ConfirmDialog
import com.brokentelephone.game.core.composable.dialog.ErrorDialog
import com.brokentelephone.game.features.create_post.content.CreatePostContent
import com.brokentelephone.game.features.create_post.dialog.chain_settings.StepperDialog
import com.brokentelephone.game.features.create_post.dialog.start_new_chain.StartNewChainDialog
import com.brokentelephone.game.features.create_post.model.ChainSetting
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onPostCreated: () -> Unit = {},
    viewModel: CreatePostViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

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
        onChainSettingClick = viewModel::onShowChainSetting,
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

    when (state.activeChainSetting) {
        ChainSetting.CHAIN_LENGTH -> StepperDialog(
            title = stringResource(R.string.create_post_chain_settings_chain_length),
            body = stringResource(R.string.create_post_chain_settings_chain_length_subtitle),
            initialValue = state.maxGenerations,
            minValue = 6,
            maxValue = 20,
            step = 2,
            valueFormatter = { stringResource(R.string.create_post_chain_settings_chain_length_value, it) },
            onDismiss = viewModel::onDismissChainSetting,
            onConfirm = viewModel::onChainLengthConfirmed,
        )
        ChainSetting.TEXT_TIME -> StepperDialog(
            title = stringResource(R.string.create_post_chain_settings_text_response),
            body = stringResource(R.string.create_post_chain_settings_text_response_subtitle),
            initialValue = state.textTimeLimit,
            minValue = 30,
            maxValue = 120,
            step = 10,
            valueFormatter = { stringResource(R.string.badge_seconds, it) },
            onDismiss = viewModel::onDismissChainSetting,
            onConfirm = viewModel::onTextTimeLimitConfirmed,
        )
        ChainSetting.DRAWING_TIME -> StepperDialog(
            title = stringResource(R.string.create_post_chain_settings_drawing_response),
            body = stringResource(R.string.create_post_chain_settings_drawing_response_subtitle),
            initialValue = state.drawingTimeLimit,
            minValue = 60,
            maxValue = 180,
            step = 10,
            valueFormatter = { stringResource(R.string.badge_seconds, it) },
            onDismiss = viewModel::onDismissChainSetting,
            onConfirm = viewModel::onDrawingTimeLimitConfirmed,
        )
        null -> Unit
    }

}
