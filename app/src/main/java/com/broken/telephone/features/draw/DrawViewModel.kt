package com.broken.telephone.features.draw

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.draw.model.DrawState
import com.broken.telephone.features.draw.model.DrawingAction
import com.broken.telephone.features.draw.model.PathData
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class DrawViewModel(
    private val postId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DrawState())
    val state = _state.asStateFlow()

    init {
        getPostByIdUseCase(postId)
            .onEach { postUi -> _state.update { it.copy(postUi = postUi) } }
            .launchIn(viewModelScope)
    }

    fun onDrawAction(action: DrawingAction) {
        when (action) {
            DrawingAction.OnClearCanvasClick -> onClearCanvasClick()
            is DrawingAction.OnDraw -> onDraw(action.offset)
            DrawingAction.OnNewPathStart -> onNewPathStart()
            DrawingAction.OnPathEnd -> onPathEnd()
            DrawingAction.OnUndoClick -> onUndo()
            DrawingAction.OnRedoClick -> onRedo()
            is DrawingAction.OnBrushSizeChange -> _state.update { it.copy(selectedBrushSize = action.brushSize) }
        }
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = null,
                paths = it.paths + currentPathData,
                redoPaths = emptyList()
            )
        }
    }

    private fun onUndo() {
        val last = state.value.paths.lastOrNull() ?: return
        _state.update {
            it.copy(
                paths = it.paths.dropLast(1),
                redoPaths = it.redoPaths + last
            )
        }
    }

    private fun onRedo() {
        val last = state.value.redoPaths.lastOrNull() ?: return
        _state.update {
            it.copy(
                paths = it.paths + last,
                redoPaths = it.redoPaths.dropLast(1)
            )
        }
    }

    private fun onNewPathStart() {
        _state.update {
            it.copy(
                currentPath = PathData(
                    id = System.currentTimeMillis().toString(),
                    color = it.selectedColor,
                    strokeWidth = it.selectedBrushSize.strokeWidth,
                    path = emptyList()
                )
            )
        }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update {
            it.copy(
                currentPath = currentPathData.copy(
                    path = currentPathData.path + offset
                )
            )
        }
    }

    private fun onClearCanvasClick() {
        _state.update {
            it.copy(
                currentPath = null,
                paths = emptyList()
            )
        }
    }
}
