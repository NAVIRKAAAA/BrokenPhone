package com.brokentelephone.game.features.chain_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.features.chain_details.model.ChainDetailsState
import com.brokentelephone.game.features.chain_details.use_case.GetChainByPostIdUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ChainDetailsViewModel(
    private val postId: String,
    private val getChainByPostIdUseCase: GetChainByPostIdUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ChainDetailsState(postId = postId))
    val state = _state.asStateFlow()

    init {
        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(currentUserId = user?.id) } }
            .launchIn(viewModelScope)

        getPostByIdUseCase(postId)
            .onEach { post -> _state.update { it.copy(post = post) } }
            .launchIn(viewModelScope)

        getChainByPostIdUseCase(postId)
            .onEach { chains -> _state.update { it.copy(chains = chains) } }
            .launchIn(viewModelScope)
    }
}
