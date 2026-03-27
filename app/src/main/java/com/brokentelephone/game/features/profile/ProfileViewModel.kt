package com.brokentelephone.game.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.GetUserContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserPostsUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.profile.model.ProfileSideEffect
import com.brokentelephone.game.features.profile.model.ProfileState
import com.brokentelephone.game.features.settings.use_case.GetAuthStateUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserPostsUseCase: GetUserPostsUseCase,
    private val getUserContributionsUseCase: GetUserContributionsUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<ProfileSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var lastLoadedAt: Long = 0L

    init {
        getAuthStateUseCase()
            .onEach { authState -> _state.update { it.copy(isAuth = authState.isAuth()) } }
            .launchIn(viewModelScope)

        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user?.toUi()) } }
            .launchIn(viewModelScope)
    }

    fun onResume() {
        if (!isLoadAllowed(lastLoadedAt)) return

        viewModelScope.launch {
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

            val posts = async { fetchMyPosts() }
            val contributions = async { fetchContributions() }
            posts.await()
            contributions.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isRefreshing = false) }
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

    fun onTabSelect(tab: ProfileTab) {
        _state.update { it.copy(selectedTab = tab) }
    }

    fun onCopyLinkClick() {
        val postId = state.value.selectedPost?.id ?: return
        val link = getPostLinkByIdUseCase(postId)
        _state.update { it.copy(isPostBottomSheetVisible = false) }
        viewModelScope.launch { _sideEffects.send(ProfileSideEffect.ShowCopyLinkSuccessToast(link)) }
    }

    fun onMoreClick(post: PostUi) {
        _state.update { it.copy(selectedPost = post, isPostBottomSheetVisible = true) }
    }

    fun onPostBottomSheetDismiss() {
        _state.update { it.copy(isPostBottomSheetVisible = false) }
    }

    fun onDeleteClick() {
        _state.update { it.copy(isPostBottomSheetVisible = false, isDeleteDialogVisible = true) }
    }

    fun onDeleteDialogDismiss() {
        _state.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onDeleteConfirm() {
        val post = state.value.selectedPost ?: return
        _state.update { it.copy(isDeleteLoading = true) }

        viewModelScope.launch {
            deletePostUseCase.execute(post.id).onSuccess {
                _state.update {
                    it.copy(
                        isDeleteLoading = false,
                        isDeleteDialogVisible = false,
                        selectedPost = null
                    )
                }

                onRefresh()
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isDeleteLoading = false,
                        isDeleteDialogVisible = false,
                        selectedPost = null,
                        globalError = exceptionToMessageMapper.map(exception)
                    )
                }
            }

        }
    }

    private companion object {
        const val COOLDOWN_MS = 30_000L
    }
}
