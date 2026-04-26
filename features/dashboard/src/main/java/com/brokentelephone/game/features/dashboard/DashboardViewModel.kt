package com.brokentelephone.game.features.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.use_case.BlockUserUseCase
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.GetUnreadNotificationsCountUseCase
import com.brokentelephone.game.domain.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.domain.use_case.ReportPostUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.dashboard.model.DashboardSideEffect
import com.brokentelephone.game.features.dashboard.model.DashboardState
import com.brokentelephone.game.features.dashboard.use_case.LoadInitialPostsUseCase
import com.brokentelephone.game.features.dashboard.use_case.LoadNextPostsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class DashboardViewModel(
    private val loadInitialPostsUseCase: LoadInitialPostsUseCase,
    private val loadNextPostsUseCase: LoadNextPostsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUnreadNotificationsCountUseCase: GetUnreadNotificationsCountUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val markPostAsNotInterestedUseCase: MarkPostAsNotInterestedUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private var lastOffset: Int = 0
    private var lastLoadedAt: Long = 0L
    private var currentSeed: String = UUID.randomUUID().toString()

    private val _sideEffects = Channel<DashboardSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user?.toUi()) } }
            .launchIn(viewModelScope)

        getUnreadNotificationsCountUseCase.execute()
            .onEach { count -> _state.update { it.copy(unreadNotificationsCount = count) } }
            .launchIn(viewModelScope)
    }

    fun loadInitialPosts() {
        if (!isInitialLoadAllowed()) return
        viewModelScope.launch {
            Log.d("LOG_TAG", "loadInitialPosts()")
            _state.update { it.copy(isInitialLoading = true) }
            currentSeed = UUID.randomUUID().toString()
            loadInitialPostsUseCase.execute(INITIAL_PAGE_SIZE, currentSeed)
                .onSuccess { page ->
                    Log.d("LOG_TAG", "loadInitialPosts: onSuccess (${page.posts.size})")
                    lastLoadedAt = System.currentTimeMillis()
                    lastOffset = page.offset
                    _state.update { state ->
                        state.copy(
                            isInitialLoading = false,
                            posts = page.posts.map { it.toUi() },
                            hasMore = page.hasMore,
                        )
                    }

                    _sideEffects.send(DashboardSideEffect.ScrollToTop)
                }
                .onError { exception ->
                    Log.d("LOG_TAG", "Error: $exception")
                    _state.update { it.copy(isInitialLoading = false) }
                }
        }
    }

    fun loadNextPosts() {
        if (state.value.isInitialLoading || state.value.isLoadingMore || !state.value.hasMore) return

        viewModelScope.launch {
            Log.d("LOG_TAG", "loadNextPosts()")
            _state.update { it.copy(isLoadingMore = true) }

            delay(150)

            loadNextPostsUseCase.execute(lastOffset, DEFAULT_PAGE_SIZE, currentSeed)
                .onSuccess { page ->
                    Log.d("LOG_TAG", "loadNextPosts: onSuccess (${page.posts.size})")
                    lastOffset = page.offset
                    _state.update { state ->
                        state.copy(
                            isLoadingMore = false,
                            posts = state.posts + page.posts.map { it.toUi() },
                            hasMore = page.hasMore,
                        )
                    }
                }
                .onError {
                    _state.update { it.copy(isLoadingMore = false) }
                }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            Log.d("LOG_TAG", "onRefresh()")
            _state.update { it.copy(isRefreshing = true) }

            delay(150)

            currentSeed = UUID.randomUUID().toString()
            loadInitialPostsUseCase.execute(INITIAL_PAGE_SIZE, currentSeed)
                .onSuccess { page ->
                    Log.d("LOG_TAG", "onRefresh: onSuccess (${page.posts.size})")
                    lastLoadedAt = System.currentTimeMillis()
                    lastOffset = page.offset
                    _state.update { state ->
                        state.copy(
                            isRefreshing = false,
                            posts = page.posts.map { it.toUi() },
                            hasMore = page.hasMore,
                        )
                    }

                    _sideEffects.send(DashboardSideEffect.ScrollToTop)
                }
                .onError {
                    _state.update { it.copy(isRefreshing = false) }
                }
        }
    }

    private fun isInitialLoadAllowed(): Boolean {
        if (lastLoadedAt == 0L) return true
        return System.currentTimeMillis() - lastLoadedAt >= REFRESH_COOLDOWN_MS
    }



    fun onCopyLinkClick() {
        val postId = state.value.selectedPost?.id ?: return
        val link = getPostLinkByIdUseCase.execute(postId)
        _state.update { it.copy(isPostBottomSheetVisible = false) }
        viewModelScope.launch { _sideEffects.send(DashboardSideEffect.ShowCopyLinkSuccessToast(link)) }
    }

    fun onMoreClick(post: PostUi) {
        _state.update { it.copy(selectedPost = post, isPostBottomSheetVisible = true) }
    }

    fun onPostBottomSheetDismiss() {
        _state.update { it.copy(isPostBottomSheetVisible = false) }
    }

    fun onNotInterestedClick() {
        val postId = state.value.selectedPost?.id ?: return
        _state.update { it.copy(isPostBottomSheetVisible = false, selectedPost = null) }

        viewModelScope.launch {
            markPostAsNotInterestedUseCase.execute(postId).onSuccess {
                onRefresh()
            }.onError { exception ->
                _state.update {
                    it.copy(globalError = exceptionToMessageMapper.map(exception))
                }
            }
        }
    }

    fun onBlockClick() {
        _state.update { it.copy(isPostBottomSheetVisible = false, isBlockDialogVisible = true) }
    }

    fun onBlockDialogDismiss() {
        _state.update { it.copy(isBlockDialogVisible = false, selectedPost = null) }
    }

    fun onBlockConfirm() {
        val userId = state.value.selectedPost?.authorId ?: return
        _state.update { it.copy(isBlockLoading = true) }

        viewModelScope.launch {
            blockUserUseCase.execute(userId).onSuccess {
                _state.update {
                    it.copy(
                        isBlockLoading = false,
                        isBlockDialogVisible = false,
                        selectedPost = null
                    )
                }
                onRefresh()
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isBlockLoading = false,
                        isBlockDialogVisible = false,
                        globalError = exceptionToMessageMapper.map(exception),
                        selectedPost = null
                    )
                }
            }
        }
    }

    fun onDeleteClick() {
        _state.update { it.copy(isPostBottomSheetVisible = false, isDeleteDialogVisible = true) }
    }

    fun onDeleteDialogDismiss() {
        _state.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun onDeleteConfirm() {
        val postId = state.value.selectedPost?.id ?: return
        _state.update { it.copy(isDeleteLoading = true) }
        viewModelScope.launch {
            deletePostUseCase.execute(postId).onSuccess {
                _state.update {
                    it.copy(
                        isDeleteLoading = false,
                        isDeleteDialogVisible = false,
                        selectedPost = null,
                    )
                }

                onRefresh()
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isDeleteLoading = false,
                        isDeleteDialogVisible = false,
                        globalError = exceptionToMessageMapper.map(exception)
                    )
                }
            }
        }
    }

    fun onReportClick() {
        _state.update {
            it.copy(
                isPostBottomSheetVisible = false,
                isReportBottomSheetVisible = true
            )
        }
    }

    fun onReportBottomSheetDismiss() {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
    }

    fun onReportTypeSelected(type: ReportPostType) {
        val postId = state.value.selectedPost?.id ?: return
        _state.update { it.copy(isReportBottomSheetVisible = false, selectedPost = null) }
        viewModelScope.launch {
            reportPostUseCase.execute(postId, type)
                .onSuccess {
                    _sideEffects.send(DashboardSideEffect.ShowReportSuccessToast)
                }.onError { exception ->
                    _state.update { it.copy(globalError = exceptionToMessageMapper.map(exception)) }
                }
        }
    }

    fun onGlobalErrorDismiss() {
        _state.update { it.copy(globalError = null) }
    }

    private companion object {
        const val REFRESH_COOLDOWN_MS = 30_000L
        const val INITIAL_PAGE_SIZE = 30
        const val DEFAULT_PAGE_SIZE = 20
    }
}
