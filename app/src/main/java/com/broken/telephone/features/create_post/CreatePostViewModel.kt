package com.broken.telephone.features.create_post

import androidx.lifecycle.ViewModel
import com.broken.telephone.features.create_post.model.CreatePostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CreatePostViewModel : ViewModel() {

    private val _state = MutableStateFlow(CreatePostState())
    val state = _state.asStateFlow()

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
        _state.update { it.copy(showStartNewChain = false) }
    }

    fun onChainSettingsConfirmed(maxGenerations: Int, textTimeLimit: Int, drawingTimeLimit: Int) {
        _state.update { it.copy(maxGenerations = maxGenerations, textTimeLimit = textTimeLimit, drawingTimeLimit = drawingTimeLimit, showChainSettings = false) }
    }

}