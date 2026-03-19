package com.brokentelephone.game.features.chain_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.chain_details.model.ChainDetailsSideEffect
import com.brokentelephone.game.features.chain_details.model.ChainDetailsState
import com.brokentelephone.game.features.chain_details.use_case.GetChainByPostIdUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChainDetailsViewModel(
    val postId: String,
    private val getChainByPostIdUseCase: GetChainByPostIdUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state =
        MutableStateFlow(ChainDetailsState(postId = postId))
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<ChainDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var lastLoadedAt: Long = 0L

    init {
        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(userUi = user) } }
            .launchIn(viewModelScope)

        loadPost()
    }

    fun onResume() {
        loadChain()
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            delay(150)

            getChainByPostIdUseCase.execute(postId).onSuccess { chain ->
                lastLoadedAt = System.currentTimeMillis()
                _state.update { it.copy(chain = chain, isRefreshing = false) }
            }.onError { e ->
                _state.update {
                    it.copy(
                        isRefreshing = false,
                        globalError = exceptionToMessageMapper.map(e),
                        globalException = e,
                    )
                }
            }

        }
    }

    private fun loadPost() {
        viewModelScope.launch {
            val post = getPostByIdUseCase.invoke(postId).firstOrNull() ?: return@launch
            _state.update { it.copy(post = post) }
        }
    }

    private fun loadChain() {
        if (!isInitialLoadAllowed()) return

        viewModelScope.launch {
            getChainByPostIdUseCase.execute(postId).onSuccess { chain ->
                lastLoadedAt = System.currentTimeMillis()
                _state.update { it.copy(chain = chain, isLoading = false) }
            }.onError { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        globalError = exceptionToMessageMapper.map(e),
                        globalException = e,
                    )
                }
            }
        }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null, globalException = null) }
        viewModelScope.launch {
            _sideEffects.send(ChainDetailsSideEffect.NavigateBack)
        }
    }

    private fun isInitialLoadAllowed(): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= REFRESH_COOLDOWN_MS
    }

    private companion object {
        const val REFRESH_COOLDOWN_MS = 30_000L
    }
}
