package com.broken.telephone.features.draw.model

import androidx.compose.ui.geometry.Offset

sealed interface DrawingAction {
    data object OnNewPathStart: DrawingAction
    data class OnDraw(val offset: Offset): DrawingAction
    data object OnPathEnd: DrawingAction
    data object OnClearCanvasClick: DrawingAction
    data object OnUndoClick: DrawingAction
    data object OnRedoClick: DrawingAction
    data class OnBrushSizeChange(val brushSize: BrushSize): DrawingAction
}