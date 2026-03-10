package com.brokentelephone.game.features.draw

import android.graphics.Bitmap
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.features.draw.model.DrawSideEffect
import com.brokentelephone.game.features.draw.model.DrawState
import com.brokentelephone.game.features.draw.model.DrawingAction
import com.brokentelephone.game.features.draw.model.PathData
import com.brokentelephone.game.features.draw.use_case.SubmitDrawingUseCase
import com.brokentelephone.game.features.draw.utils.DrawingBitmapSaver
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import kotlinx.coroutines.Job
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
    private val countdownTimer: CountdownTimer,
) : ViewModel() {

    private val _state = MutableStateFlow(DrawState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DrawSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        getPostByIdUseCase(postId)
            .onEach { postUi ->
                _state.update { it.copy(postUi = postUi) }
                if (postUi != null && timerJob == null) {
                    startTimer(postUi.nextTimeLimit)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun startTimer(timeLimit: Int) {
        timerJob = countdownTimer.start(timeLimit)
            .onEach { remaining ->
                _state.update { it.copy(remainingSeconds = remaining) }
                if (remaining == 0) {
                    _state.update {
                        it.copy(
                            isTimerExpired = true,
                            showTimesUpDialog = true,
                            showDiscardDialog = false,
                            showPostConfirmDialog = false
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun onDrawAction(action: DrawingAction) {
        when (action) {
            DrawingAction.OnClearCanvasClick -> if (!state.value.isTimerExpired) onClearCanvasClick()
            is DrawingAction.OnDraw -> if (!state.value.isTimerExpired) onDraw(action.offset)
            DrawingAction.OnNewPathStart -> if (!state.value.isTimerExpired) onNewPathStart()
            DrawingAction.OnPathEnd -> if (!state.value.isTimerExpired) onPathEnd()
            DrawingAction.OnUndoClick -> onUndo()
            DrawingAction.OnRedoClick -> onRedo()
            is DrawingAction.OnBrushSizeChange -> _state.update { it.copy(selectedBrushSize = action.brushSize) }
            is DrawingAction.OnCanvasSizeChanged -> _state.update { it.copy(canvasSize = action.size) }
            DrawingAction.OnPostClick -> onPostClick()
            DrawingAction.OnPostConfirm -> onPostConfirm()
            DrawingAction.OnPostDismiss -> _state.update { it.copy(showPostConfirmDialog = false) }
            DrawingAction.OnBackClick -> onBackClick()
            DrawingAction.OnDiscardConfirm -> onDiscardConfirm()
            DrawingAction.OnDiscardDismiss -> _state.update { it.copy(showDiscardDialog = false) }
            DrawingAction.OnTimesUpGotIt -> {
                _state.update { it.copy(showTimesUpDialog = false) }
                viewModelScope.launch { _sideEffects.send(DrawSideEffect.NavigateBack) }
            }
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

    private fun onBackClick() {
        if (state.value.hasChanges) {
            _state.update { it.copy(showDiscardDialog = true) }
        } else {
            viewModelScope.launch { _sideEffects.send(DrawSideEffect.NavigateBack) }
        }
    }

    private fun onDiscardConfirm() {
        _state.update { it.copy(showDiscardDialog = false) }
        viewModelScope.launch { _sideEffects.send(DrawSideEffect.NavigateBack) }
    }

    private fun onPostClick() {
        _state.update { it.copy(showPostConfirmDialog = true) }
    }

    private fun onPostConfirm() {
        val currentState = state.value
        val canvasSize = currentState.canvasSize ?: return
        timerJob?.cancel()
        _state.update { it.copy(showPostConfirmDialog = false, isPosting = true) }

        viewModelScope.launch {
            val bitmap = renderToBitmap(currentState.paths, canvasSize.width, canvasSize.height)
            val localPath = drawingBitmapSaver.save(bitmap)
            submitDrawingUseCase(postId, localPath)

            _state.update { it.copy(isPosting = false) }

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
