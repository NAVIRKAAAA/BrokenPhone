package com.brokentelephone.game.core.model.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

sealed interface DrawingCanvasAction {
    data class OnColorChange(val color: Color) : DrawingCanvasAction
    data object OnNewPathStart : DrawingCanvasAction
    data class OnDraw(val offset: Offset) : DrawingCanvasAction
    data object OnPathEnd : DrawingCanvasAction
    data object OnClearCanvasClick : DrawingCanvasAction
    data object OnUndoClick : DrawingCanvasAction
    data object OnRedoClick : DrawingCanvasAction
    data class OnBrushSizeChange(val brushSize: BrushSize) : DrawingCanvasAction
    data class OnCanvasSizeChanged(val size: IntSize) : DrawingCanvasAction
}
