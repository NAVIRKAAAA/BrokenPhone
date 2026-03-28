package com.brokentelephone.game.features.friends

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.GetFriendsUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.friends.model.FriendsSideEffect
import com.brokentelephone.game.features.friends.model.FriendsState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class FriendsViewModel(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(FriendsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<FriendsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var friends: List<UserUi> = emptyList()
    private var lastLoadedAt: Long = 0L

    init {
        viewModelScope.launch {
            _state
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(300)
                .collect { query ->
                    applyFilter(query)
                    _sideEffects.send(FriendsSideEffect.ScrollToTop)
                }
        }
    }

    fun onResume() {
        loadFriends()
    }

    private fun loadFriends() {
        Log.d("LOG_TAG", "loadFriends()")
        if (!isLoadAllowed()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getFriendsUseCase.execute()
                .onSuccess { result ->
                    Log.d("LOG_TAG", "loadFriends: onSuccess (${result.size})")
                    lastLoadedAt = System.currentTimeMillis()
                    friends = result.map { it.toUi() }
                    _state.update {
                        it.copy(
                            filteredFriends = applyFilterTo(friends, it.searchQuery),
                            isLoading = false,
                            loadError = null,
                        )
                    }
                }
                .onError { exception ->
                    Log.d("LOG_TAG", "loadFriends: onError ($exception)")
                    _state.update {
                        it.copy(
                            isLoading = false,
                            loadError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            delay(150)
            getFriendsUseCase.execute()
                .onSuccess { result ->
                    lastLoadedAt = System.currentTimeMillis()
                    friends = result.map { it.toUi() }
                    _state.update {
                        it.copy(
                            filteredFriends = applyFilterTo(friends, it.searchQuery),
                            isRefreshing = false,
                        )
                    }
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

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onSearchClear() {
        _state.update { it.copy(searchQuery = "") }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }

    private fun applyFilter(query: String) {
        _state.update { it.copy(filteredFriends = applyFilterTo(friends, query)) }
    }

    private fun applyFilterTo(list: List<UserUi>, query: String): List<UserUi> =
        if (query.isBlank()) list else list.filter { it.username.contains(query, ignoreCase = true) }

    private fun isLoadAllowed(): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= REFRESH_COOLDOWN_MS
    }

    private companion object {
        const val REFRESH_COOLDOWN_MS = 30_000L
    }
}
