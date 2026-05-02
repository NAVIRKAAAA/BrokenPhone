package com.brokentelephone.game.features.draw.model

import com.brokentelephone.game.core.model.draw.DrawingCanvasAction

sealed interface DrawingAction {
    data class OnCanvasAction(val action: DrawingCanvasAction) : DrawingAction
    data object OnPostClick : DrawingAction
    data object OnBackClick : DrawingAction
    data object OnDiscardConfirm : DrawingAction
    data object OnDiscardDismiss : DrawingAction
    data object OnTimesUpGotIt : DrawingAction
    data object OnGlobalErrorDismiss : DrawingAction
}
