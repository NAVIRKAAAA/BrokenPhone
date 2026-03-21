package com.brokentelephone.game.core.modifier

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.hidden(cornerRadius: Dp = 8.dp): Modifier {
    val baseColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val highlightColor = MaterialTheme.colorScheme.surfaceVariant
    val shimmerColors = listOf(baseColor, highlightColor, baseColor)

    return drawWithCache {
        val brush = Brush.linearGradient(colors = shimmerColors)
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
