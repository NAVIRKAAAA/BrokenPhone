package com.broken.telephone.features.describe_drawing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                DescribeDrawingSideEffect.PostCreated -> onPostSubmitted()
            }
        }
    }

    DescribeDrawingContent(
        state = state,
        onBackClick = onBackClick,
        onTextChanged = viewModel::onTextChanged,
        onPostClick = viewModel::onPostClick,
        modifier = modifier
    )
}
