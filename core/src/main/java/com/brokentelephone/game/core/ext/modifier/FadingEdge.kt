package com.brokentelephone.game.core.ext.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.horizontalFadingEdge(
    startWidth: Dp = 16.dp,
    endWidth: Dp = 16.dp,
): Modifier = graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        val startEdge = (startWidth.toPx() / size.width).coerceIn(0f, 1f)
        val endEdge = (1f - endWidth.toPx() / size.width).coerceIn(0f, 1f)
        drawRect(
            brush = Brush.horizontalGradient(
                colorStops = arrayOf(
                    0f to Color.Black.copy(alpha = 0.25f),
                    (startEdge * 0.4f) to Color.Black.copy(alpha = 0.5f),
                    (startEdge * 0.7f) to Color.Black.copy(alpha = 0.8f),
                    startEdge to Color.Black,
                    endEdge to Color.Black,
                    (endEdge + (1f - endEdge) * 0.3f) to Color.Black.copy(alpha = 0.8f),
                    (endEdge + (1f - endEdge) * 0.6f) to Color.Black.copy(alpha = 0.5f),
                    1f to Color.Black.copy(alpha = 0.25f),
                ),
            ),
            blendMode = BlendMode.DstIn,
        )
    }
