package com.broken.telephone.core.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val HiddenColors = listOf(
    Color(0xFFE3E3E3),
    Color(0xFFF5F5F5),
    Color(0xFFE3E3E3)
)

fun Modifier.hidden(cornerRadius: Dp = 8.dp): Modifier = drawWithCache {
    val brush = Brush.linearGradient(colors = HiddenColors)
    val cornerPx = cornerRadius.toPx()
    onDrawWithContent {
        drawRoundRect(
            brush = brush,
            cornerRadius = CornerRadius(cornerPx, cornerPx),
            size = size
        )
    }
}
