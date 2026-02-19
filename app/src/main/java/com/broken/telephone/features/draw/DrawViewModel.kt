package com.broken.telephone.features.draw

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.draw.model.DrawSideEffect
import com.broken.telephone.features.draw.model.DrawState
import com.broken.telephone.features.draw.model.DrawingAction
import com.broken.telephone.features.draw.model.PathData
import com.broken.telephone.features.draw.use_case.SubmitDrawingUseCase
import com.broken.telephone.features.draw.utils.DrawingBitmapSaver
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

class DrawViewModel(
    private val postId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val drawingBitmapSaver: DrawingBitmapSaver,
    private val submitDrawingUseCase: SubmitDrawingUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DrawState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DrawSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

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
            is DrawingAction.OnCanvasSizeChanged -> _state.update { it.copy(canvasSize = action.size) }
            DrawingAction.OnPostClick -> onPostClick()
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

    private fun onPostClick() {
        val currentState = state.value
        val canvasSize = currentState.canvasSize ?: return

        viewModelScope.launch {
            val bitmap = renderToBitmap(currentState.paths, canvasSize.width, canvasSize.height)
            val localPath = drawingBitmapSaver.save(bitmap)
            submitDrawingUseCase(postId, localPath)
            _sideEffects.send(DrawSideEffect.PostCreated(localPath))
        }
    }

    private fun renderToBitmap(paths: List<PathData>, width: Int, height: Int): Bitmap {
        val bitmap = createBitmap(width, height)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        paths.forEach { pathData ->
            val paint = Paint().apply {
                color = pathData.color.toArgb()
                style = Paint.Style.STROKE
                strokeWidth = pathData.strokeWidth
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                isAntiAlias = true
            }

            val path = android.graphics.Path()
            if (pathData.path.isNotEmpty()) {
                path.moveTo(pathData.path.first().x, pathData.path.first().y)
                val smoothness = 5
                for (i in 1..pathData.path.lastIndex) {
                    val from = pathData.path[i - 1]
                    val to = pathData.path[i]
                    val dx = abs(from.x - to.x)
                    val dy = abs(from.y - to.y)
                    if (dx >= smoothness || dy >= smoothness) {
                        path.quadTo(
                            (from.x + to.x) / 2f,
                            (from.y + to.y) / 2f,
                            to.x,
                            to.y
                        )
                    }
                }
            }

            canvas.drawPath(path, paint)
        }

        return bitmap
    }

}
