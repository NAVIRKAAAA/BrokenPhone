package com.brokentelephone.game.core.shimmer

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShimmerEffect(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.shimmer())
}
