package com.brokentelephone.game.features.chain_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.brokentelephone.game.features.chain_details.content.ChainDetailsContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChainDetailsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChainDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ChainDetailsContent(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier,
    )
}
