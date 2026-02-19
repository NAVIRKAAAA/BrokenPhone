package com.broken.telephone.features.post_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.post_details.content.PostDetailsContent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PostDetailsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: PostDetailsViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PostDetailsContent(
        state = state,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
