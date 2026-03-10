package com.brokentelephone.game.features.describe_drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.features.describe_drawing.model.DescribeDrawingSideEffect
import com.brokentelephone.game.features.describe_drawing.model.DescribeDrawingState
import com.brokentelephone.game.features.describe_drawing.use_case.SubmitDescriptionUseCase
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

class DescribeDrawingViewModel(
    private val postId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val submitDescriptionUseCase: SubmitDescriptionUseCase,
    private val countdownTimer: CountdownTimer,
) : ViewModel() {

    private val _state = MutableStateFlow(DescribeDrawingState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DescribeDrawingSideEffect>(Channel.BUFFERED)
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

    fun onTextChanged(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun onBackClick() {
        if (state.value.hasChanges) {
            _state.update { it.copy(showDiscardDialog = true) }
        } else {
            viewModelScope.launch { _sideEffects.send(DescribeDrawingSideEffect.NavigateBack) }
        }
    }

    fun onDiscardConfirm() {
        _state.update { it.copy(showDiscardDialog = false) }
        viewModelScope.launch { _sideEffects.send(DescribeDrawingSideEffect.NavigateBack) }
    }

    fun onDiscardDismiss() {
        _state.update { it.copy(showDiscardDialog = false) }
    }

    fun onTimesUpGotIt() {
        _state.update { it.copy(showTimesUpDialog = false) }
        viewModelScope.launch { _sideEffects.send(DescribeDrawingSideEffect.NavigateBack) }
    }

    fun onPostClick() {
        if (state.value.isTimerExpired) return
        val text = state.value.text.trim()
        if (text.isBlank()) return
        _state.update { it.copy(showPostConfirmDialog = true) }
    }

    fun onPostConfirm() {
        val text = state.value.text.trim()
        timerJob?.cancel()
        _state.update { it.copy(showPostConfirmDialog = false, isPosting = true) }
        viewModelScope.launch {
            submitDescriptionUseCase(postId, text)

            _state.update { it.copy(isPosting = false) }

            _sideEffects.send(DescribeDrawingSideEffect.PostCreated)
        }
    }

    fun onPostDismiss() {
        _state.update { it.copy(showPostConfirmDialog = false) }
    }
}
