package com.brokentelephone.game.core.model.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PathData(
    val id: String,
    val color: Color,
    val strokeWidth: Float,
    val path: List<Offset>,
)