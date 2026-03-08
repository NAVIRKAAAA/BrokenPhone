package com.broken.telephone.core.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.shimmer(cornerRadius: Dp = 0.dp): Modifier {
    val baseColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val highlightColor = MaterialTheme.colorScheme.surfaceVariant
    val shimmerColors = listOf(baseColor, highlightColor, baseColor)

    val transition = rememberInfiniteTransition(label = "Shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600,
                easing = LinearEasing
            )
        ),
        label = "Translate"
    )

    return this.drawWithCache {
        val gradientWidth = size.width * 0.7f
        val margin = size.width * 1.5f
        val startX = -(gradientWidth + margin) + (size.width + gradientWidth + margin * 2f) * progress
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(startX, 0f),
            end = Offset(startX + gradientWidth, size.height)
        )
        val cornerPx = cornerRadius.toPx()
        onDrawWithContent {
            drawRoundRect(
                brush = brush,
                cornerRadius = CornerRadius(cornerPx, cornerPx),
                size = size
            )
        }
    }
}
