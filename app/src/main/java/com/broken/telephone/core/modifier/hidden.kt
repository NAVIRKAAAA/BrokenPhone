package com.broken.telephone.core.modifier

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
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val surface = MaterialTheme.colorScheme.surface
    return drawWithCache {
        val brush = Brush.linearGradient(colors = listOf(surfaceVariant, surface, surfaceVariant))
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
