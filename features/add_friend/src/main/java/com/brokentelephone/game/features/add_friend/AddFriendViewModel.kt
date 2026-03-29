package com.brokentelephone.game.features.add_friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.add_friend.model.AddFriendSideEffect
import com.brokentelephone.game.features.add_friend.model.AddFriendState
import com.brokentelephone.game.features.add_friend.use_case.GetPendingInvitesUseCase
import com.brokentelephone.game.features.add_friend.use_case.GetReceivedPendingInvitesUseCase
import com.brokentelephone.game.features.add_friend.use_case.SearchUsersUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AddFriendViewModel(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val getPendingInvitesUseCase: GetPendingInvitesUseCase,
    private val getReceivedPendingInvitesUseCase: GetReceivedPendingInvitesUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(AddFriendState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddFriendSideEffect>()
    val event = _event.asSharedFlow()

    private var lastLoadedAt: Long = 0L

    init {
        _state
            .map { it.searchQuery }
            .distinctUntilChanged()
            .drop(1)
            .debounce(400)
            .onEach { query ->
                if (query.isBlank()) {
                    _state.update { it.copy(results = emptyList(), isSearching = false) }
                } else {
                    search(query)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onResume() {
        if (!isLoadAllowed()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val pendingInvites = async { fetchPendingInvites() }
            val receivedPendingInvites = async { fetchReceivedPendingInvites() }
            pendingInvites.await()
            receivedPendingInvites.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun fetchPendingInvites() {
        getPendingInvitesUseCase.execute()
            .onSuccess { pendingInvites ->
                _state.update { it.copy(pendingInvites = pendingInvites) }
            }
    }

    private suspend fun fetchReceivedPendingInvites() {
        getReceivedPendingInvitesUseCase.execute()
            .onSuccess { pendingInvites ->
                _state.update { it.copy(receivedPendingInvites = pendingInvites) }
            }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(150)

            val pendingInvites = async { fetchPendingInvites() }
            val receivedPendingInvites = async { fetchReceivedPendingInvites() }
            pendingInvites.await()
            receivedPendingInvites.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun search(query: String) {
        _state.update { it.copy(isSearching = true) }

        searchUsersUseCase.execute(query)
            .onSuccess { results ->
                val excludedIds = (_state.value.pendingInvites + _state.value.receivedPendingInvites)
                    .map { it.user.id }.toSet()
                val filtered = results.filter { it.user.id !in excludedIds }
                _state.update { it.copy(results = filtered, isSearching = false) }

                _event.emit(AddFriendSideEffect.ScrollToTop)
            }
            .onError { exception ->
                _state.update {
                    it.copy(isSearching = false)
                }
            }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onSearchClear() {
        _state.update { it.copy(searchQuery = "", results = emptyList()) }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }

    private fun isLoadAllowed(): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= REFRESH_COOLDOWN_MS
    }

    private companion object {
        const val REFRESH_COOLDOWN_MS = 30_000L
    }
}
