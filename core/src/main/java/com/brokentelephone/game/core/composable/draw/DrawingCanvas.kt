package com.brokentelephone.game.core.composable.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.util.fastForEach
import com.brokentelephone.game.core.model.draw.DrawingCanvasAction
import com.brokentelephone.game.core.model.draw.PathData
import com.brokentelephone.game.core.theme.appColors
import kotlin.math.abs

// TODO: Think about zoom feature

@Composable
fun DrawingCanvas(
    paths: List<PathData>,
    currentPath: PathData?,
    onAction: (DrawingCanvasAction) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(MaterialTheme.appColors.canvasBg)
            .onSizeChanged { onAction(DrawingCanvasAction.OnCanvasSizeChanged(it)) }
            .then(
                if (enabled) Modifier.pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitPointerEvent().changes.first()
                        onAction(DrawingCanvasAction.OnNewPathStart)
                        onAction(DrawingCanvasAction.OnDraw(down.position))
                        down.consume()
                        try {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull() ?: break
                                if (!change.pressed) break
                                change.consume()
                                onAction(DrawingCanvasAction.OnDraw(change.position))
                            }
                        } finally {
                            onAction(DrawingCanvasAction.OnPathEnd)
                        }
                    }
                } else Modifier
            )
    ) {
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                thickness = pathData.strokeWidth,
            )
        }
        currentPath?.let {
            drawPath(
                path = it.path,
                color = it.color,
                thickness = it.strokeWidth,
            )
        }
    }
}

private fun DrawScope.drawPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f
) {
    val smoothedPath = Path().apply {
        if (path.isNotEmpty()) {
            moveTo(path.first().x, path.first().y)

            val smoothness = 5
            for (i in 1..path.lastIndex) {
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)
                if (dx >= smoothness || dy >= smoothness) {
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                } else {
                    lineTo(to.x, to.y)
                }
            }
        }
    }
    if (path.size == 1) {
        drawCircle(color = color, radius = thickness / 2f, center = path.first())
    } else {
        drawPath(
            path = smoothedPath,
            color = color,
            style = Stroke(
                width = thickness,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}
