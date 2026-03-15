package com.brokentelephone.game.core.shimmer

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private enum class ShimmerState { LOADING, EMPTY, CONTENT }

@Composable
fun ShimmerContent(
    isLoading: Boolean,
    shimmerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    isEmpty: Boolean = false,
    emptyContent: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val state = when {
        isLoading -> ShimmerState.LOADING
        isEmpty -> ShimmerState.EMPTY
        else -> ShimmerState.CONTENT
    }

    AnimatedContent(
        targetState = state,
        modifier = modifier,
    ) { s ->
        when (s) {
            ShimmerState.LOADING -> shimmerContent()
            ShimmerState.EMPTY -> emptyContent()
            ShimmerState.CONTENT -> content()
        }
    }
}
