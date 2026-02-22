package com.broken.telephone.features.post_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.features.post_details.model.PostDetailsSideEffect
import com.broken.telephone.features.post_details.model.PostDetailsState
import com.broken.telephone.features.post_details.use_case.BlockUserUseCase
import com.broken.telephone.features.post_details.use_case.DeletePostUseCase
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import com.broken.telephone.features.post_details.use_case.GetPostLinkByIdUseCase
import com.broken.telephone.features.post_details.use_case.MockStartGameUseCase
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

class PostDetailsViewModel(
    private val postId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val notInterestedUseCase: NotInterestedUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val mockStartGameUseCase: MockStartGameUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PostDetailsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<PostDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var currentUserId: String? = null

    init {
        getCurrentUserUseCase()
            .onEach { user -> currentUserId = user?.id }
            .launchIn(viewModelScope)

        getPostByIdUseCase(postId)
            .onEach { postUi -> _state.update { it.copy(postUi = postUi, isCurrentUserPost = postUi?.authorId == currentUserId) } }
            .launchIn(viewModelScope)
    }

    fun onCopyLinkClick() {
        val link = getPostLinkByIdUseCase(postId)
        _state.update { it.copy(isBottomSheetVisible = false) }
        viewModelScope.launch { _sideEffects.send(PostDetailsSideEffect.ShowCopyLinkSuccessToast(link)) }
    }

    fun onMoreClick() {
        _state.update { it.copy(isBottomSheetVisible = true) }
    }

    fun onBottomSheetDismiss() {
        _state.update { it.copy(isBottomSheetVisible = false) }
    }

    fun onReportClick() {
        _state.update { it.copy(isBottomSheetVisible = false, isReportBottomSheetVisible = true) }
    }

    fun onBlockClick() {
        _state.update { it.copy(isBottomSheetVisible = false, isBlockDialogVisible = true) }
    }

    fun onDeleteClick() {
        _state.update { it.copy(isBottomSheetVisible = false, isDeleteDialogVisible = true) }
    }

    fun onDeleteDialogDismiss() {
        _state.update { it.copy(isDeleteDialogVisible = false) }
    }

    fun onDeleteConfirm() {
        _state.update { it.copy(isDeleteLoading = true) }
        viewModelScope.launch {
            deletePostUseCase(postId)
            _state.update { it.copy(isDeleteLoading = false, isDeleteDialogVisible = false) }
            _sideEffects.send(PostDetailsSideEffect.NavigateBack)
        }
    }

    fun onBlockDialogDismiss() {
        _state.update { it.copy(isBlockDialogVisible = false) }
    }

    fun onNotInterestedClick() {
        val postParentId = state.value.postUi?.parentId ?: return
        _state.update { it.copy(isBottomSheetVisible = false) }
        viewModelScope.launch {
            notInterestedUseCase(postParentId)
            _sideEffects.send(PostDetailsSideEffect.NavigateBack)
        }
    }

    fun onBlockConfirm() {
        val blockedUserId = state.value.postUi?.authorId ?: return
        _state.update { it.copy(isBlockLoading = true) }
        viewModelScope.launch {
            blockUserUseCase(blockedUserId)
            _state.update { it.copy(isBlockLoading = false, isBlockDialogVisible = false) }
            _sideEffects.send(PostDetailsSideEffect.NavigateBack)
        }
    }

    fun onContinueClick() {
        val post = state.value.postUi ?: return
        _state.update { it.copy(isContinueLoading = true) }
        viewModelScope.launch {
            mockStartGameUseCase(postId)
            _state.update { it.copy(isContinueLoading = false) }
            val effect = when (post.content) {
                is PostContent.Text -> PostDetailsSideEffect.NavigateToDraw(postId)
                is PostContent.Drawing -> PostDetailsSideEffect.NavigateToDescribeDrawing(postId)
            }
            _sideEffects.send(effect)
        }
    }

    fun onReportBottomSheetDismiss() {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
    }

    fun onReportTypeSelected(type: ReportPostType) {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
        viewModelScope.launch { reportPostUseCase(postId, type) }
        viewModelScope.launch { _sideEffects.send(PostDetailsSideEffect.ShowReportSuccessToast) }
    }
}
