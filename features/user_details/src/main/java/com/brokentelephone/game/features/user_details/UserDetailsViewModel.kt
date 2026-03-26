package com.brokentelephone.game.features.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.use_case.GetUserContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserPostsUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.user_details.model.UserDetailsState
import com.brokentelephone.game.features.user_details.use_case.GetFriendshipActionStateUseCase
import com.brokentelephone.game.features.user_details.use_case.GetUserByIdUseCase
import com.brokentelephone.game.features.user_details.use_case.SendFriendRequestUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val userId: String,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserContributionsUseCase: GetUserContributionsUseCase,
    private val getFriendshipActionStateUseCase: GetFriendshipActionStateUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(UserDetailsState())
    val state = _state.asStateFlow()

    private var lastLoadedAt: Long = 0L


    fun onResume() {
        if (!isLoadAllowed(lastLoadedAt)) return

        viewModelScope.launch {
            val user = async { fetchUser() }
            val friendshipActionState = async { fetchFriendshipActionState() }

            user.await()
            friendshipActionState.await()

            _state.update { it.copy(isPostsLoading = true, isContributionsLoading = true) }

            val posts = async { fetchMyPosts() }
            val contributions = async { fetchContributions() }
            posts.await()
            contributions.await()
            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isInitialLoading = false) }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }

            delay(150)

            val user = async { fetchUser() }
            val friendshipActionState = async { fetchFriendshipActionState() }

            user.await()
            friendshipActionState.await()

            val posts = async { fetchMyPosts() }
            val contributions = async { fetchContributions() }
            posts.await()
            contributions.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun fetchFriendshipActionState() {
        getFriendshipActionStateUseCase.execute(userId)
            .onSuccess { friendshipState ->
                _state.update { it.copy(friendshipActionState = friendshipState) }
            }
    }

    private suspend fun fetchUser() {
        _state.update { it.copy(isUserLoading = true) }

        getUserByIdUseCase.execute(userId)
            .onSuccess { user ->
                _state.update { it.copy(isUserLoading = false, user = user?.toUi()) }
            }
            .onError { exception ->
                _state.update {
                    it.copy(
                        isUserLoading = false,
                        globalError = exceptionToMessageMapper.map(exception),
                    )
                }
            }
    }

    private suspend fun fetchMyPosts() {
        val user = state.value.user ?: return

        getUserPostsUseCase.execute(user.id)
            .onSuccess { posts ->
                _state.update {
                    it.copy(
                        isPostsLoading = false,
                        myPosts = posts.map { p -> p.toUi() })
                }
            }
            .onError {
                _state.update { it.copy(isPostsLoading = false) }
            }
    }

    private suspend fun fetchContributions() {
        val user = state.value.user ?: return

        getUserContributionsUseCase.execute(user.id)
            .onSuccess { posts ->
                _state.update {
                    it.copy(
                        isContributionsLoading = false,
                        myContributions = posts.map { p -> p.toUi() })
                }
            }
            .onError {
                _state.update { it.copy(isContributionsLoading = false) }
            }
    }

    private fun isLoadAllowed(lastLoadedAt: Long): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= COOLDOWN_MS
    }

    fun onAddFriendClick() {
        viewModelScope.launch {
            _state.update { it.copy(isFriendshipActionLoading = true) }
            sendFriendRequestUseCase.execute(userId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isFriendshipActionLoading = false,
                            friendshipActionState = FriendshipActionState.INVITE_SENT,
                        )
                    }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            isFriendshipActionLoading = false,
                            globalError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    fun onTabSelect(tab: ProfileTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }

    private companion object {
        const val COOLDOWN_MS = 30_000L
    }
}
