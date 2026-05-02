package com.brokentelephone.game.features.create_post

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.draw.DrawingCanvasAction
import com.brokentelephone.game.core.model.draw.PathData
import com.brokentelephone.game.core.model.tab_row.create_post.CreatePostTab
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.core.utils.DrawingBitmapSaver
import com.brokentelephone.game.core.utils.renderToBitmap
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.create_post.model.ChainSetting
import com.brokentelephone.game.features.create_post.model.CreatePostState
import com.brokentelephone.game.features.create_post.use_case.CreatePostUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CreatePostSideEffect {
    data object PostCreated : CreatePostSideEffect
    data object NavigateBack : CreatePostSideEffect
}

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val drawingBitmapSaver: DrawingBitmapSaver,
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePostState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<CreatePostSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        getCurrentUserUseCase.execute()
            .onEach { user -> _state.update { it.copy(user = user?.toUi()) } }
            .launchIn(viewModelScope)
    }

    fun onDrawAction(action: DrawingCanvasAction) {
        when (action) {
            DrawingCanvasAction.OnClearCanvasClick -> onClearCanvasClick()
            is DrawingCanvasAction.OnDraw -> onDraw(action.offset)
            DrawingCanvasAction.OnNewPathStart -> onNewPathStart()
            DrawingCanvasAction.OnPathEnd -> onPathEnd()
            DrawingCanvasAction.OnUndoClick -> onUndo()
            DrawingCanvasAction.OnRedoClick -> onRedo()
            is DrawingCanvasAction.OnBrushSizeChange -> _state.update { it.copy(selectedBrushSize = action.brushSize) }
            is DrawingCanvasAction.OnColorChange -> _state.update { it.copy(selectedColor = action.color) }
            is DrawingCanvasAction.OnCanvasSizeChanged -> _state.update { it.copy(canvasSize = action.size) }
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

    fun onTabSelect(tab: CreatePostTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onBackClick() {
        if (_state.value.text.isNotBlank()) {
            _state.update { it.copy(showDiscardDialog = true) }
        } else {
            viewModelScope.launch { _sideEffect.send(CreatePostSideEffect.NavigateBack) }
        }
    }

    fun onDiscardDismiss() {
        _state.update { it.copy(showDiscardDialog = false) }
    }

    fun onDiscardConfirm() {
        _state.update { it.copy(showDiscardDialog = false) }
        viewModelScope.launch { _sideEffect.send(CreatePostSideEffect.NavigateBack) }
    }

    fun onTextChanged(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun onShowChainSetting(setting: ChainSetting) {
        _state.update { it.copy(activeChainSetting = setting) }
    }

    fun onDismissChainSetting() {
        _state.update { it.copy(activeChainSetting = null) }
    }

    fun onShowStartNewChain() {
        _state.update { it.copy(showStartNewChain = true) }
    }

    fun onDismissStartNewChain() {
        if (_state.value.isLoading) return
        _state.update { it.copy(showStartNewChain = false) }
    }

    fun onChainLengthConfirmed(value: Int) {
        _state.update { it.copy(maxGenerations = value, activeChainSetting = null) }
    }

    fun onTextTimeLimitConfirmed(value: Int) {
        _state.update { it.copy(textTimeLimit = value, activeChainSetting = null) }
    }

    fun onDrawingTimeLimitConfirmed(value: Int) {
        _state.update { it.copy(drawingTimeLimit = value, activeChainSetting = null) }
    }

    fun onStartChain() {
        val current = _state.value
        val canvasSize = current.canvasSize ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val selectedTab = current.selectedTab
            val content = when(selectedTab) {
                CreatePostTab.TEXT -> {
                    PostContent.Text(current.text)
                }
                CreatePostTab.DRAW -> {
                    val bitmap = renderToBitmap(current.paths, canvasSize.width, canvasSize.height)
                    val localPath = drawingBitmapSaver.save(bitmap)

                    PostContent.Drawing(localPath = localPath)
                }
            }

            createPostUseCase.execute(
                content = content,
                maxGenerations = current.maxGenerations,
                textTimeLimit = current.textTimeLimit,
                drawingTimeLimit = current.drawingTimeLimit,
            ).onSuccess {
                _state.update { it.copy(isLoading = false, showStartNewChain = false) }
                _sideEffect.send(CreatePostSideEffect.PostCreated)
            }.onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        showStartNewChain = false,
                        globalError = exceptionToMessageMapper.map(error)
                    )
                }
            }

        }
    }

}
