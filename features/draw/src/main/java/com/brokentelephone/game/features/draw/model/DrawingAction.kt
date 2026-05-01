package com.brokentelephone.game.features.draw.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

sealed interface DrawingAction {
    data class OnColorChange(val color: Color) : DrawingAction
    data object OnNewPathStart: DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnPathEnd: DrawingAction
    data object OnClearCanvasClick: DrawingAction
    data object OnUndoClick: DrawingAction
    data object OnRedoClick: DrawingAction
    data class OnBrushSizeChange(val brushSize: BrushSize): DrawingAction
    data class OnCanvasSizeChanged(val size: IntSize): DrawingAction
    data object OnPostClick: DrawingAction
    data object OnBackClick: DrawingAction
    data object OnDiscardConfirm: DrawingAction
    data object OnDiscardDismiss: DrawingAction
    data object OnTimesUpGotIt: DrawingAction
    data object OnGlobalErrorDismiss: DrawingAction
}