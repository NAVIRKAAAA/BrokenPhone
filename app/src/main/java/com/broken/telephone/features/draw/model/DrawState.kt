package com.broken.telephone.features.draw.model

import androidx.compose.ui.graphics.Color
import com.broken.telephone.features.dashboard.model.PostUi

data class DrawState(
    val postUi: PostUi? = null,
    val selectedColor: Color = Color.Black,
    val selectedBrushSize: BrushSize = BrushSize.MEDIUM,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
) {
    val canUndo: Boolean get() = paths.isNotEmpty()
    val canRedo: Boolean get() = redoPaths.isNotEmpty()
}
