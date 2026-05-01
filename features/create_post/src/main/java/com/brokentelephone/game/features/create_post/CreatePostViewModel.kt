package com.brokentelephone.game.features.create_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
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
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            createPostUseCase.execute(
                text = current.text,
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
