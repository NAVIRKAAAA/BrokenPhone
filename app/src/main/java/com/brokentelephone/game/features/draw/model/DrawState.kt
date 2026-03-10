package com.brokentelephone.game.features.draw.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.brokentelephone.game.features.dashboard.model.PostUi

data class DrawState(
    val postUi: PostUi? = null,
    val selectedColor: Color = Color.Black,
    val selectedBrushSize: BrushSize = BrushSize.MEDIUM,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
    val canvasSize: IntSize? = null,
    val showDiscardDialog: Boolean = false,
    val showPostConfirmDialog: Boolean = false,
    val isPosting: Boolean = false,
    val remainingSeconds: Int = 0,
    val isTimerExpired: Boolean = false,
    val showTimesUpDialog: Boolean = false,
) {
    val canUndo: Boolean get() = paths.isNotEmpty()
    val canRedo: Boolean get() = redoPaths.isNotEmpty()
    val hasChanges: Boolean get() = paths.isNotEmpty()

    val formattedTime: String get() {
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
