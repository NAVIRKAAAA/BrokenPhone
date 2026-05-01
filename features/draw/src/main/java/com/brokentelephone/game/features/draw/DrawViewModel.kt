package com.brokentelephone.game.features.draw

import android.graphics.Bitmap
import android.graphics.Paint
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.CancelSessionUseCase
import com.brokentelephone.game.domain.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.domain.use_case.GetPostByIdUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.draw.model.DrawSideEffect
import com.brokentelephone.game.features.draw.model.DrawState
import com.brokentelephone.game.features.draw.model.DrawingAction
import com.brokentelephone.game.features.draw.model.PathData
import com.brokentelephone.game.features.draw.use_case.SubmitDrawingUseCase
import com.brokentelephone.game.features.draw.utils.DrawingBitmapSaver
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs

class DrawViewModel(
    private val sessionId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val drawingBitmapSaver: DrawingBitmapSaver,
    private val submitDrawingUseCase: SubmitDrawingUseCase,
    private val cancelSessionUseCase: CancelSessionUseCase,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(DrawState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DrawSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        loadinInitialData()
    }

    private fun loadinInitialData() {
        viewModelScope.launch {
            getActiveSessionUseCase.execute(sessionId).onSuccess { session ->
                _state.update { it.copy(session = session) }

                Log.d("LOG_TAG", "Session: $session")

                getPostByIdUseCase.executeWithResult(session.postId).onSuccess { post ->
                    _state.update { it.copy(postUi = post.toUi()) }
                    startTimer()
                }.onError { e ->
                    _state.update {
                        it.copy(globalError = exceptionToMessageMapper.map(e))
                    }
                }

            }.onError { e ->
                _state.update {
                    it.copy(globalError = exceptionToMessageMapper.map(e))
                }
            }
        }
    }

    private fun startTimer() {
        val sessionExpiresAt = state.value.session?.expiresAt ?: 0L

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                val remaining = ((sessionExpiresAt - System.currentTimeMillis()) / 1000).toInt()
                    .coerceAtLeast(0)
                _state.update { it.copy(remainingSeconds = remaining) }
                if (remaining == 0) {
                    _state.update {
                        it.copy(
                            isTimerExpired = true,
                            showTimesUpDialog = true,
                            showDiscardDialog = false,
                        )
                    }
                    break
                }
                delay(1000)
            }
        }
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
            DrawingAction.OnBackClick -> onBackClick()
            DrawingAction.OnDiscardConfirm -> onDiscardConfirm()
            DrawingAction.OnDiscardDismiss -> _state.update { it.copy(showDiscardDialog = false) }
            DrawingAction.OnTimesUpGotIt -> onTimesUpGotIt()
            DrawingAction.OnGlobalErrorDismiss -> _state.update { it.copy(globalError = null) }
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

    private fun onTimesUpGotIt() {
        _state.update { it.copy(isCancelling = true) }
        viewModelScope.launch {
            cancelSessionUseCase.execute(sessionId).onSuccess {
                _state.update { it.copy(isCancelling = false, showTimesUpDialog = false) }
                _sideEffects.send(DrawSideEffect.NavigateBack)
            }.onError {
                _state.update { it.copy(isCancelling = false) }
            }
        }
    }

    private fun onBackClick() {
        _state.update { it.copy(showDiscardDialog = true) }
    }

    private fun onDiscardConfirm() {
        timerJob?.cancel()
        _state.update { it.copy(isCancelling = true) }
        viewModelScope.launch {
            cancelSessionUseCase.execute(sessionId).onSuccess {
                _state.update { it.copy(isCancelling = false, showDiscardDialog = false) }
                _sideEffects.send(DrawSideEffect.NavigateBack)
            }.onError {
                _state.update { it.copy(isCancelling = false) }
            }
        }
    }

    private fun onPostClick() {
        val currentState = state.value
        val canvasSize = currentState.canvasSize ?: return

        timerJob?.cancel()
        _state.update { it.copy(isPosting = true) }

        viewModelScope.launch {
            val bitmap = renderToBitmap(currentState.paths, canvasSize.width, canvasSize.height)
            val localPath = drawingBitmapSaver.save(bitmap)
            submitDrawingUseCase.execute(sessionId, localPath).onSuccess {
                _sideEffects.send(DrawSideEffect.PostCreated(localPath))
            }.onError { _ ->
                // HANDLE
            }
        }
    }

    private fun renderToBitmap(paths: List<PathData>, width: Int, height: Int): Bitmap {
        val bitmap = createBitmap(width, height)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.TRANSPARENT)

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
