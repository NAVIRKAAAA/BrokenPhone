package com.broken.telephone.features.create_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.create_post.model.CreatePostState
import com.broken.telephone.features.create_post.use_case.CreatePostUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CreatePostSideEffect {
    data object PostCreated : CreatePostSideEffect
}

class CreatePostViewModel(
    private val createPostUseCase: CreatePostUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreatePostState())
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<CreatePostSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    fun onTextChanged(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun onShowChainSettings() {
        _state.update { it.copy(showChainSettings = true) }
    }

    fun onDismissChainSettings() {
        _state.update { it.copy(showChainSettings = false) }
    }

    fun onShowStartNewChain() {
        _state.update { it.copy(showStartNewChain = true) }
    }

    fun onDismissStartNewChain() {
        if (_state.value.isLoading) return
        _state.update { it.copy(showStartNewChain = false) }
    }

    fun onChainSettingsConfirmed(maxGenerations: Int, textTimeLimit: Int, drawingTimeLimit: Int) {
        _state.update { it.copy(maxGenerations = maxGenerations, textTimeLimit = textTimeLimit, drawingTimeLimit = drawingTimeLimit, showChainSettings = false) }
    }

    fun onStartChain() {
        val current = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            createPostUseCase(
                text = current.text,
                maxGenerations = current.maxGenerations,
                textTimeLimit = current.textTimeLimit,
                drawingTimeLimit = current.drawingTimeLimit,
            )
            _state.update { it.copy(isLoading = false, showStartNewChain = false) }
            _sideEffect.send(CreatePostSideEffect.PostCreated)
        }
    }

}
