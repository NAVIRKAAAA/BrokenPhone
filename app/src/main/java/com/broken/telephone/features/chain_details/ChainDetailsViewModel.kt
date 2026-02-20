package com.broken.telephone.features.chain_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.chain_details.model.ChainDetailsState
import com.broken.telephone.features.chain_details.use_case.GetChainByPostIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ChainDetailsViewModel(
    private val postId: String,
    private val getChainByPostIdUseCase: GetChainByPostIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ChainDetailsState(postId = postId))
    val state = _state.asStateFlow()

    init {
        getChainByPostIdUseCase(postId)
            .onEach { chains -> _state.update { it.copy(chains = chains) } }
            .launchIn(viewModelScope)
    }
}
