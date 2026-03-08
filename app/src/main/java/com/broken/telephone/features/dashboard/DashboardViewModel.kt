package com.broken.telephone.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.features.dashboard.model.DashboardSideEffect
import com.broken.telephone.features.dashboard.model.DashboardSort
import com.broken.telephone.features.dashboard.model.DashboardState
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.dashboard.use_case.GetPostsUseCase
import com.broken.telephone.features.post_details.use_case.BlockUserUseCase
import com.broken.telephone.features.post_details.use_case.DeletePostUseCase
import com.broken.telephone.features.post_details.use_case.GetPostLinkByIdUseCase
import com.broken.telephone.features.post_details.use_case.NotInterestedUseCase
import com.broken.telephone.features.post_details.use_case.ReportPostUseCase
import com.broken.telephone.features.profile.use_case.GetCurrentUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getPostsUseCase: GetPostsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val notInterestedUseCase: NotInterestedUseCase,
    private val reportPostUseCase: ReportPostUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<DashboardSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getPostsUseCase()
            .onEach { posts -> _state.update { it.copy(posts = posts, isLoading = false) } }
            .launchIn(viewModelScope)

        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user) } }
            .launchIn(viewModelScope)
    }

    fun onSortSelected(sort: DashboardSort) {
        _state.update { it.copy(selectedSort = sort) }
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
        val postParentId = state.value.selectedPost?.parentId ?: return
        _state.update { it.copy(isPostBottomSheetVisible = false, selectedPost = null) }
        viewModelScope.launch {
            notInterestedUseCase(postParentId)
        }
        viewModelScope.launch {
            _sideEffects.send(DashboardSideEffect.ShowNotInterestedToast)
        }
    }

    fun onBlockClick() {
        _state.update { it.copy(isPostBottomSheetVisible = false, isBlockDialogVisible = true) }
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
            deletePostUseCase(postId)
            _state.update { it.copy(isDeleteLoading = false, isDeleteDialogVisible = false, selectedPost = null) }
        }
    }

    fun onBlockDialogDismiss() {
        _state.update { it.copy(isBlockDialogVisible = false, selectedPost = null) }
    }

    fun onBlockConfirm() {
        val blockedUserId = state.value.selectedPost?.authorId ?: return
        _state.update { it.copy(isBlockLoading = true) }
        viewModelScope.launch {
            blockUserUseCase(blockedUserId)
            _state.update { it.copy(isBlockLoading = false, isBlockDialogVisible = false, selectedPost = null) }
        }
    }

    fun onReportClick() {
        _state.update { it.copy(isPostBottomSheetVisible = false, isReportBottomSheetVisible = true) }
    }

    fun onReportBottomSheetDismiss() {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
    }

    fun onReportTypeSelected(type: ReportPostType) {
        val postId = state.value.selectedPost?.id ?: return
        _state.update { it.copy(isReportBottomSheetVisible = false, selectedPost = null) }
        viewModelScope.launch { reportPostUseCase(postId, type) }
        viewModelScope.launch { _sideEffects.send(DashboardSideEffect.ShowReportSuccessToast) }
    }

}
