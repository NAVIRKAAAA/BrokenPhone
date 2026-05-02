package com.brokentelephone.game.core.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import com.brokentelephone.game.core.model.draw.PathData
import kotlin.math.abs

// TODO: Handle errors

fun renderToBitmap(paths: List<PathData>, width: Int, height: Int): Bitmap {
    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.TRANSPARENT)

    paths.forEach { pathData ->
        val paint = Paint().apply {
            color = pathData.color.toArgb()
            style = Paint.Style.STROKE
            strokeWidth = pathData.strokeWidth
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

        if (pathData.path.size == 1) {
            val center = pathData.path.first()
            paint.style = Paint.Style.FILL
            canvas.drawCircle(center.x, center.y, pathData.strokeWidth / 2f, paint)
        } else if (pathData.path.isNotEmpty()) {
            val path = Path()
            path.moveTo(pathData.path.first().x, pathData.path.first().y)
            val smoothness = 5
            for (i in 1..pathData.path.lastIndex) {
                val from = pathData.path[i - 1]
                val to = pathData.path[i]
                val dx = abs(from.x - to.x)
                val dy = abs(from.y - to.y)
                if (dx >= smoothness || dy >= smoothness) {
                    path.quadTo(
                        (from.x + to.x) / 2f,
                        (from.y + to.y) / 2f,
                        to.x,
                        to.y
                    )
                } else {
                    path.lineTo(to.x, to.y)
                }
            }
            canvas.drawPath(path, paint)
        }
    }

    return bitmap
}
