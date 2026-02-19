package com.broken.telephone.features.describe_drawing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.describe_drawing.model.DescribeDrawingSideEffect
import com.broken.telephone.features.describe_drawing.model.DescribeDrawingState
import com.broken.telephone.features.describe_drawing.use_case.SubmitDescriptionUseCase
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
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
) : ViewModel() {

    private val _state = MutableStateFlow(DescribeDrawingState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DescribeDrawingSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getPostByIdUseCase(postId)
            .onEach { postUi -> _state.update { it.copy(postUi = postUi) } }
            .launchIn(viewModelScope)
    }

    fun onTextChanged(text: String) {
        _state.update { it.copy(text = text) }
    }

    fun onPostClick() {
        val text = state.value.text.trim()
        if (text.isBlank()) return
        viewModelScope.launch {
            submitDescriptionUseCase(postId, text)
            _sideEffects.send(DescribeDrawingSideEffect.PostCreated)
        }
    }
}
