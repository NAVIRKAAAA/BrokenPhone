package com.brokentelephone.game.features.create_post.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.brokentelephone.game.core.model.draw.BrushSize
import com.brokentelephone.game.core.model.draw.PathData
import com.brokentelephone.game.core.model.tab_row.create_post.CreatePostTab
import com.brokentelephone.game.core.model.user.UserUi

data class CreatePostState(
    val text: String = "",
    val user: UserUi? = null,
    val generation: Int = 0,
    val maxGenerations: Int = DEFAULT_MAX_GENERATIONS,
    val textTimeLimit: Int = DEFAULT_TEXT_TIME_LIMIT,
    val drawingTimeLimit: Int = DEFAULT_DRAWING_TIME_LIMIT,
    val activeChainSetting: ChainSetting? = null,
    val showStartNewChain: Boolean = false,
    val showDiscardDialog: Boolean = false,
    val isLoading: Boolean = false,
    val globalError: String? = null,
    val selectedTab: CreatePostTab = CreatePostTab.TEXT,
    val selectedColor: Color = Color.Black,
    val selectedBrushSize: BrushSize = BrushSize.MEDIUM,
    val currentPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val redoPaths: List<PathData> = emptyList(),
    val canvasSize: IntSize? = null,
) {
    val isTextOverLimit: Boolean get() = text.length > MAX_TEXT_LENGTH

    val canUndo: Boolean get() = paths.isNotEmpty()
    val canRedo: Boolean get() = redoPaths.isNotEmpty()

    val isSubmitButtonEnabled: Boolean
        get() = when(selectedTab) {
            CreatePostTab.TEXT -> text.isNotBlank() && !isTextOverLimit
            CreatePostTab.DRAW -> canUndo && !isLoading
        }

    companion object {
        const val MAX_TEXT_LENGTH = 140
        const val DEFAULT_MAX_GENERATIONS = 10
        const val DEFAULT_TEXT_TIME_LIMIT = 60
        const val DEFAULT_DRAWING_TIME_LIMIT = 120
    }
}