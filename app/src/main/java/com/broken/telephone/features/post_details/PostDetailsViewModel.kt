package com.broken.telephone.features.post_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.post_details.model.PostDetailsState
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class PostDetailsViewModel(
    private val postId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PostDetailsState())
    val state = _state.asStateFlow()

    init {
        getPostByIdUseCase(postId)
            .onEach { postUi -> _state.update { it.copy(postUi = postUi) } }
            .launchIn(viewModelScope)
    }
}
