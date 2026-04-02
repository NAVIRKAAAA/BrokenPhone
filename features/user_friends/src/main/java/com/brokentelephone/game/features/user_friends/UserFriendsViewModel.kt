package com.brokentelephone.game.features.user_friends

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.user.AddFriendUserUi
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.use_case.AcceptFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.CancelFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.GetFriendsUseCase
import com.brokentelephone.game.domain.use_case.GetSuggestedUsersUseCase
import com.brokentelephone.game.domain.use_case.GetUserByIdUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.domain.use_case.SendFriendRequestUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.user_friends.model.UserFriendsSideEffect
import com.brokentelephone.game.features.user_friends.model.UserFriendsState
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
class UserFriendsViewModel(
    private val userId: String,
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val cancelFriendRequestUseCase: CancelFriendRequestUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val getSuggestedUsersUseCase: GetSuggestedUsersUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(UserFriendsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<UserFriendsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var lastLoadedAt: Long = 0L
    private var friends: List<AddFriendUserUi> = emptyList()

    init {
        viewModelScope.launch {
            _state
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(300)
                .collect { query ->
                    applyFilter(query)
                    _sideEffects.send(UserFriendsSideEffect.ScrollToTop)
                }
        }
    }


    fun onResume() {
        if (!isLoadAllowed()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val user = async { fetchUser() }
            val friends = async { fetchFriends() }
            val suggestedUsers = async { fetchSuggestedUsers() }

            user.await()
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

            val user = async { fetchUser() }
            val friends = async { fetchFriends() }
            val suggestedUsers = async { fetchSuggestedUsers() }

            user.await()
            friends.await()
            suggestedUsers.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun fetchUser() {
        getUserByIdUseCase.execute(userId)
            .onSuccess { user ->
                _state.update { it.copy(user = user?.toUi()) }
            }
            .onError { exception ->
                _state.update {
                    it.copy(
                        globalError = exceptionToMessageMapper.map(exception),
                    )
                }
            }
    }


    private suspend fun fetchFriends() {
        getFriendsUseCase.execute(userId)
            .onSuccess { result ->
                val userFriends = result.map { (user, friendState) ->
                    AddFriendUserUi(user.toUi(), friendState)
                }

                friends = userFriends

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

    fun onSearchQueryChange(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun onSearchClear() {
        _state.update { it.copy(searchQuery = "") }
    }

    fun onRemoveFriendClick(friendId: String) {
        _state.update { it.copy(removeFriendDialogUserId = friendId) }
    }

    fun onRemoveFriendDialogDismiss() {
        _state.update { it.copy(removeFriendDialogUserId = null) }
    }

    fun onCancelRequestClick(targetUserId: String) {
        _state.update { it.copy(cancelRequestDialogUserId = targetUserId) }
    }

    fun onCancelRequestDialogDismiss() {
        _state.update { it.copy(cancelRequestDialogUserId = null) }
    }


    fun onAcceptRequestClick(senderUserId: String) {
        viewModelScope.launch {
            _state.update { it.copy(acceptingUserIds = it.acceptingUserIds + senderUserId) }
            acceptFriendRequestUseCase.execute(senderUserId)
                .onSuccess {
                    friends = friends.map { item ->
                        if (item.user.id == senderUserId) item.copy(friendshipState = FriendshipActionState.FRIENDS)
                        else item
                    }
                    _state.update {
                        it.copy(
                            acceptingUserIds = it.acceptingUserIds - senderUserId,
                            filteredFriends = applyFilterTo(friends, it.searchQuery)
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
                        )
                    }

                    onRefresh()
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

    fun onAddFriendClick(targetUserId: String) {
        viewModelScope.launch {
            _state.update { it.copy(addingFriendUserIds = it.addingFriendUserIds + targetUserId) }
            sendFriendRequestUseCase.execute(targetUserId)
                .onSuccess {
                    friends = friends.map { item ->
                        if (item.user.id == targetUserId) item.copy(friendshipState = FriendshipActionState.INVITE_SENT)
                        else item
                    }
                    _state.update {
                        it.copy(
                            addingFriendUserIds = it.addingFriendUserIds - targetUserId,
                            filteredFriends = applyFilterTo(friends, it.searchQuery)
                        )
                    }

                    onRefresh()
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            addingFriendUserIds = it.addingFriendUserIds - targetUserId,
                            globalError = exceptionToMessageMapper.map(error),
                        )
                    }
                }
        }
    }

    fun onRemoveFriendConfirm() {
        val friendId = _state.value.removeFriendDialogUserId ?: return
        _state.update { it.copy(isRemoveFriendLoading = true) }
        viewModelScope.launch {
            removeFriendUseCase.execute(friendId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isRemoveFriendLoading = false,
                            removeFriendDialogUserId = null,
                        )
                    }

                    onRefresh()
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isRemoveFriendLoading = false,
                            removeFriendDialogUserId = null,
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

    private fun applyFilterTo(list: List<AddFriendUserUi>, query: String): List<AddFriendUserUi> =
        if (query.isBlank()) list else list.filter { item ->
            val user = item.user

            user.username.contains(
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
