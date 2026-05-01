package com.brokentelephone.game.features.friends

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.user.AddFriendUserUi
import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.use_case.AcceptFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.CancelFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.GetFriendsUseCase
import com.brokentelephone.game.domain.use_case.GetSuggestedUsersUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.domain.use_case.SendFriendRequestUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.friends.model.FriendsSideEffect
import com.brokentelephone.game.features.friends.model.FriendsState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
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
    private val getSuggestedUsersUseCase: GetSuggestedUsersUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val cancelFriendRequestUseCase: CancelFriendRequestUseCase,
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

        if (!isLoadAllowed()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val friends = async { fetchFriends() }
            val suggestedUsers = async { fetchSuggestedUsers() }

            friends.await()
            suggestedUsers.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isLoading = false) }
        }

    }

    fun onRefresh() {

        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            delay(150)

            val friends = async { fetchFriends() }
            val suggestedUsers = async { fetchSuggestedUsers() }

            friends.await()
            suggestedUsers.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun fetchFriends() {
        getFriendsUseCase.execute()
            .onSuccess { result ->
                Log.d("LOG_TAG", "fetchFriends: onSuccess (${result.size})")
                friends = result.map { it.toUi() }
                _state.update {
                    it.copy(filteredFriends = applyFilterTo(friends, it.searchQuery))
                }
            }.onError { exception ->
                Log.d("LOG_TAG", "fetchFriends: onError ($exception)")
                // TODO: handle
            }
    }

    private suspend fun fetchSuggestedUsers() {
        getSuggestedUsersUseCase.execute().onSuccess { result ->

            val suggestedUsers = result.map { (user, friendState) ->
                AddFriendUserUi(user.toUi(), friendState)
            }

            _state.update { state ->
                state.copy(suggestedUsers = suggestedUsers)
            }
        }.onError { exception ->
            Log.d("LOG_TAG", "fetchSuggestedUsers: onError ($exception)")
            // TODO: handle
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onSearchClear() {
        _state.update { it.copy(searchQuery = "") }
    }

    fun onRemoveFriendClick(friendId: String) {
        _state.update { it.copy(selectedFriendId = friendId) }
    }

    fun onRemoveFriendDialogDismiss() {
        _state.update { it.copy(selectedFriendId = null) }
    }

    fun onRemoveFriendConfirm() {
        val friendId = _state.value.selectedFriendId ?: return
        _state.update { it.copy(isRemoveFriendLoading = true) }
        viewModelScope.launch {
            removeFriendUseCase.execute(friendId)
                .onSuccess {
                    onRefresh()

                    _state.update {
                        it.copy(
                            selectedFriendId = null,
                            isRemoveFriendLoading = false,
                        )
                    }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            selectedFriendId = null,
                            isRemoveFriendLoading = false,
                            globalError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    fun onAcceptRequestClick(senderUserId: String) {
        viewModelScope.launch {
            _state.update { it.copy(acceptingUserIds = it.acceptingUserIds + senderUserId) }
            acceptFriendRequestUseCase.execute(senderUserId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            acceptingUserIds = it.acceptingUserIds - senderUserId,
                            suggestedUsers = it.suggestedUsers.filter { item -> item.user.id != senderUserId },
                        )
                    }
                    onRefresh()
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            acceptingUserIds = it.acceptingUserIds - senderUserId,
                            globalError = exceptionToMessageMapper.map(error),
                        )
                    }
                }
        }
    }

    fun onSuggestedAddFriendClick(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(sendingRequestUserIds = it.sendingRequestUserIds + userId) }
            sendFriendRequestUseCase.execute(userId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            sendingRequestUserIds = it.sendingRequestUserIds - userId,
                            suggestedUsers = it.suggestedUsers.map { item ->
                                if (item.user.id == userId) item.copy(friendshipState = FriendshipActionState.INVITE_SENT)
                                else item
                            },
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            sendingRequestUserIds = it.sendingRequestUserIds - userId,
                            globalError = exceptionToMessageMapper.map(error),
                        )
                    }
                }
        }
    }

    fun onCancelRequestClick(targetUserId: String) {
        _state.update { it.copy(cancelRequestDialogUserId = targetUserId) }
    }

    fun onCancelRequestDialogDismiss() {
        _state.update { it.copy(cancelRequestDialogUserId = null) }
    }

    fun onCancelRequestConfirm() {
        val targetUserId = _state.value.cancelRequestDialogUserId ?: return
        _state.update { it.copy(isCancelRequestLoading = true) }

        viewModelScope.launch {
            cancelFriendRequestUseCase.execute(targetUserId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isCancelRequestLoading = false,
                            cancelRequestDialogUserId = null,
                            suggestedUsers = it.suggestedUsers.map { item ->
                                if (item.user.id == targetUserId) item.copy(friendshipState = FriendshipActionState.NOT_FRIENDS)
                                else item
                            },
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isCancelRequestLoading = false,
                            cancelRequestDialogUserId = null,
                            globalError = exceptionToMessageMapper.map(error),
                        )
                    }
                }
        }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }

    private fun applyFilter(query: String) {
        _state.update { it.copy(filteredFriends = applyFilterTo(friends, query)) }
    }

    private fun applyFilterTo(list: List<UserUi>, query: String): List<UserUi> =
        if (query.isBlank()) list else list.filter {
            it.username.contains(
                query,
                ignoreCase = true
            )
        }

    private fun isLoadAllowed(): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= REFRESH_COOLDOWN_MS
    }

    private companion object {
        const val REFRESH_COOLDOWN_MS = 30_000L
    }
}
