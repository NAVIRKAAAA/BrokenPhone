package com.broken.telephone.features.post_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.features.post_details.model.PostDetailsSideEffect
import com.broken.telephone.features.post_details.model.PostDetailsState
import com.broken.telephone.features.post_details.use_case.BlockUserUseCase
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import com.broken.telephone.features.post_details.use_case.NotInterestedUseCase
import com.broken.telephone.features.post_details.use_case.ReportPostUseCase
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
    private val reportPostUseCase: ReportPostUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val notInterestedUseCase: NotInterestedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(PostDetailsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<PostDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getPostByIdUseCase(postId)
            .onEach { postUi -> _state.update { it.copy(postUi = postUi) } }
            .launchIn(viewModelScope)
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

    fun onReportBottomSheetDismiss() {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
    }

    fun onReportTypeSelected(type: ReportPostType) {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
        viewModelScope.launch { reportPostUseCase(postId, type) }
        viewModelScope.launch { _sideEffects.send(PostDetailsSideEffect.ShowReportSuccessToast) }
    }
}
