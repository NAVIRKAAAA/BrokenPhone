package com.brokentelephone.game.features.draw.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.domain.model.session.GameSession

data class DrawState(
    val postUi: PostUi? = null,
    val session: GameSession? = null,
    val selectedColor: Color = Color.Black,
    val selectedBrushSize: BrushSize = BrushSize.MEDIUM,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
    val canvasSize: IntSize? = null,
    val showDiscardDialog: Boolean = false,
    val isCancelling: Boolean = false,
    val isPosting: Boolean = false,
    val remainingSeconds: Int = 0,
    val isTimerExpired: Boolean = false,
    val showTimesUpDialog: Boolean = false,
    val globalError: String? = null,
) {
    val canUndo: Boolean get() = paths.isNotEmpty()
    val canRedo: Boolean get() = redoPaths.isNotEmpty()

    val formattedTime: String get() {
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
