package com.brokentelephone.game.core.modifier

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp

fun Modifier.coloredShadow(
    color: Color,
    blurRadius: Float,
    offsetY: Dp,
    offsetX: Dp,
    shape: Shape
) = then(
    this.drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()

            paint.color = color

            if (blurRadius != 0f) {
                frameworkPaint.setMaskFilter(blurRadius)
            }

            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()

            val path = Path().apply {
                addOutline(shape.createOutline(size, layoutDirection, this@drawBehind))
            }
            canvas.translate(leftPixel, topPixel)
            canvas.drawPath(path, paint)
            canvas.translate(-leftPixel, -topPixel)
        }
    }
)

private fun NativePaint.setMaskFilter(blurRadius: Float) {
    this.maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
}