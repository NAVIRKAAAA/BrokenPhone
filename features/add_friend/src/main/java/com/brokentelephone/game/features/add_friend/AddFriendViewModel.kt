package com.brokentelephone.game.features.add_friend

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.add_friend.model.AddFriendSideEffect
import com.brokentelephone.game.features.add_friend.model.AddFriendState
import com.brokentelephone.game.features.add_friend.use_case.GetPendingInvitesUseCase
import com.brokentelephone.game.features.add_friend.use_case.SearchUsersUseCase
import kotlinx.coroutines.FlowPreview
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
            getPendingInvitesUseCase.execute()
                .onSuccess { pendingInvites ->
                    lastLoadedAt = System.currentTimeMillis()
                    _state.update { it.copy(pendingInvites = pendingInvites, isLoading = false) }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            globalError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(150)

            getPendingInvitesUseCase.execute()
                .onSuccess { pendingInvites ->
                    lastLoadedAt = System.currentTimeMillis()
                    _state.update { it.copy(pendingInvites = pendingInvites, isRefreshing = false) }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            globalError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    private suspend fun search(query: String) {
        _state.update { it.copy(isSearching = true) }
        Log.d("LOG_TAG", "search: $query")

        searchUsersUseCase.execute(query)
            .onSuccess { results ->
                Log.d("LOG_TAG", "search: ${results.size}")
                val pendingIds = _state.value.pendingInvites.map { it.user.id }.toSet()
                val filtered = results.filter { it.user.id !in pendingIds }
                _state.update { it.copy(results = filtered, isSearching = false) }

                _event.emit(AddFriendSideEffect.ScrollToTop)
            }
            .onError { exception ->
                Log.d("LOG_TAG", "search: $exception")
                _state.update {
                    it.copy(
                        isSearching = false,
                        globalError = exceptionToMessageMapper.map(exception),
                    )
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
