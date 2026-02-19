package com.broken.telephone.features.draw

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.draw.content.DrawContent
import com.broken.telephone.features.draw.model.DrawSideEffect
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

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is DrawSideEffect.PostCreated -> onPostSubmitted()
            }
        }
    }

    DrawContent(
        state = state,
        onDrawAction = viewModel::onDrawAction,
        onBackClick = onBackClick,
        modifier = modifier
    )
}
