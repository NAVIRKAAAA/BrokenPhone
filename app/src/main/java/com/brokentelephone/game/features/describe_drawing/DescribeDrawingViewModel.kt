package com.brokentelephone.game.features.describe_drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.describe_drawing.model.DescribeDrawingSideEffect
import com.brokentelephone.game.features.describe_drawing.model.DescribeDrawingState
import com.brokentelephone.game.features.describe_drawing.use_case.SubmitDescriptionUseCase
import com.brokentelephone.game.features.draw.use_case.CancelSessionUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.main.use_case.GetActiveSessionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class DescribeDrawingViewModel(
    private val sessionId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val submitDescriptionUseCase: SubmitDescriptionUseCase,
    private val cancelSessionUseCase: CancelSessionUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DescribeDrawingState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DescribeDrawingSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch { loadinInitialData() }
    }

    private fun loadinInitialData() {
        viewModelScope.launch {
            getActiveSessionUseCase.execute().onSuccess { session ->
                _state.update { it.copy(session = session) }

                getPostByIdUseCase.executeWithResult(session.postId).onSuccess { postUi ->
                    _state.update { it.copy(postUi = postUi) }
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
                            showPostConfirmDialog = false
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

    fun onTextChanged(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun onBackClick() {
        _state.update { it.copy(showDiscardDialog = true) }
    }

    fun onDiscardConfirm() {
        timerJob?.cancel()
        _state.update { it.copy(isCancelling = true) }
        viewModelScope.launch {
            val postId = state.value.postUi?.id ?: return@launch

            cancelSessionUseCase.execute(sessionId, postId).onSuccess {
                _state.update { it.copy(isCancelling = false, showDiscardDialog = false) }
                _sideEffects.send(DescribeDrawingSideEffect.NavigateBack)
            }.onError {
                _state.update { it.copy(isCancelling = false) }
            }
        }
    }

    fun onDiscardDismiss() {
        _state.update { it.copy(showDiscardDialog = false) }
    }

    fun onTimesUpGotIt() {
        _state.update { it.copy(isCancelling = true) }
        viewModelScope.launch {
            val postId = state.value.postUi?.id ?: return@launch

            cancelSessionUseCase.execute(sessionId, postId).onSuccess {
                _state.update { it.copy(isCancelling = false, showTimesUpDialog = false) }
                _sideEffects.send(DescribeDrawingSideEffect.NavigateBack)
            }.onError {
                _state.update { it.copy(isCancelling = false) }
            }
        }
    }

    fun onPostClick() {
        if (state.value.isTimerExpired) return
        val text = state.value.text.trim()
        if (text.isBlank()) return
        _state.update { it.copy(showPostConfirmDialog = true) }
    }

    fun onPostConfirm() {
        val text = state.value.text.trim()
        val post = state.value.postUi ?: return

        timerJob?.cancel()
        _state.update { it.copy(showPostConfirmDialog = false, isPosting = true) }
        viewModelScope.launch {
            submitDescriptionUseCase.execute(post.id, text)
                .onSuccess {
                    _state.update { it.copy(isPosting = false) }

                    delay(150)

                    _sideEffects.send(DescribeDrawingSideEffect.PostCreated)
                }
                .onError { _ ->
                    // HANDLE
                }
        }
    }

    fun onPostDismiss() {
        _state.update { it.copy(showPostConfirmDialog = false) }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }
}
