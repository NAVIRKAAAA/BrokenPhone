package com.brokentelephone.game.features.chain_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.core.dialog.ErrorDialog
import com.brokentelephone.game.features.chain_details.content.ChainDetailsContent
import com.brokentelephone.game.features.chain_details.model.ChainDetailsSideEffect
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChainDetailsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChainDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                ChainDetailsSideEffect.NavigateBack -> onBackClick()
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onResume()
    }

    ChainDetailsContent(
        state = state,
        onBackClick = onBackClick,
        onRefresh = viewModel::onRefresh,
        modifier = modifier,
    )

    state.globalError?.let { error ->
        ErrorDialog(
            body = error,
            onOkClick = viewModel::onGlobalErrorDismiss,
        )
    }
}
