package com.brokentelephone.game.features.dashboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.dashboard.model.DashboardSideEffect
import com.brokentelephone.game.features.dashboard.model.DashboardState
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.dashboard.model.toUi
import com.brokentelephone.game.features.dashboard.use_case.LoadInitialPostsUseCase
import com.brokentelephone.game.features.dashboard.use_case.LoadNextPostsUseCase
import com.brokentelephone.game.features.post_details.use_case.BlockUserUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.features.post_details.use_case.ReportPostUseCase
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val loadInitialPostsUseCase: LoadInitialPostsUseCase,
    private val loadNextPostsUseCase: LoadNextPostsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val markPostAsNotInterestedUseCase: MarkPostAsNotInterestedUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private var lastDocRef: DocumentSnapshot? = null
    private var lastLoadedAt: Long = 0L

    private val _sideEffects = Channel<DashboardSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user) } }
            .launchIn(viewModelScope)
    }

    fun loadInitialPosts() {
        if (!isInitialLoadAllowed()) return
        viewModelScope.launch {
            Log.d("LOG_TAG", "loadInitialPosts()")
            _state.update { it.copy(isInitialLoading = true) }
            loadInitialPostsUseCase.execute(INITIAL_PAGE_SIZE, state.value.selectedSort)
                .onSuccess { page ->
                    Log.d("LOG_TAG", "loadInitialPosts: onSuccess (${page.posts.size})")
                    lastLoadedAt = System.currentTimeMillis()
                    lastDocRef = page.lastDocRef
                    _state.update { state ->
                        state.copy(
                            isInitialLoading = false,
                            posts = page.posts.map { it.toUi() },
                            hasMore = page.hasMore,
                        )
                    }

                    delay(500)

                    _sideEffects.send(DashboardSideEffect.ScrollToTop)
                }
                .onError { exception ->
                    Log.d("LOG_TAG", "Error: $exception")
                    _state.update { it.copy(isInitialLoading = false) }
                }
        }
    }

    fun loadNextPosts() {
        val docRef = lastDocRef ?: return

        if (state.value.isInitialLoading || state.value.isLoadingMore || !state.value.hasMore) return

        viewModelScope.launch {
            Log.d("LOG_TAG", "loadNextPosts()")
            _state.update { it.copy(isLoadingMore = true) }

            delay(150)

            loadNextPostsUseCase.execute(docRef, DEFAULT_PAGE_SIZE, state.value.selectedSort)
                .onSuccess { page ->
                    Log.d("LOG_TAG", "loadNextPosts: onSuccess (${page.posts.size})")
                    lastDocRef = page.lastDocRef
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

            loadInitialPostsUseCase.execute(INITIAL_PAGE_SIZE, state.value.selectedSort)
                .onSuccess { page ->
                    Log.d("LOG_TAG", "onRefresh: onSuccess (${page.posts.size})")
                    lastLoadedAt = System.currentTimeMillis()
                    lastDocRef = page.lastDocRef
                    _state.update { state ->
                        state.copy(
                            isRefreshing = false,
                            posts = page.posts.map { it.toUi() },
                            hasMore = page.hasMore,
                        )
                    }

                    delay(500)

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

    fun onSortSelected(sort: DashboardSort) {
        _state.update { it.copy(selectedSort = sort) }
        lastLoadedAt = 0L
        loadInitialPosts()
    }

    fun onCopyLinkClick() {
        val postId = state.value.selectedPost?.id ?: return
        val link = getPostLinkByIdUseCase(postId)
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
//        val postParentId = state.value.selectedPost?.parentId ?: return
//        _state.update { it.copy(isPostBottomSheetVisible = false, selectedPost = null) }
//
//        viewModelScope.launch {
//            markPostAsNotInterestedUseCase.execute(postParentId).onSuccess {
//                onRefresh()
//            }.onError { exception ->
//                _state.update {
//                    it.copy(globalError = exceptionToMessageMapper.map(exception))
//                }
//            }
//        }
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
