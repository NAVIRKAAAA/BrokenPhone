package com.brokentelephone.game.core.shimmer

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShimmerContent(
    isLoading: Boolean,
    shimmerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = isLoading,
        modifier = modifier,
    ) { loading ->
        if (loading) shimmerContent() else content()
    }
}
