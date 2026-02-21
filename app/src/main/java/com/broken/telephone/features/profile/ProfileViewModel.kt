package com.broken.telephone.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.broken.telephone.features.dashboard.model.PostUi
import com.broken.telephone.features.post_details.use_case.BlockUserUseCase
import com.broken.telephone.features.post_details.use_case.NotInterestedUseCase
import com.broken.telephone.features.post_details.use_case.ReportPostUseCase
import com.broken.telephone.features.profile.model.ProfileSideEffect
import com.broken.telephone.features.profile.model.ProfileState
import com.broken.telephone.features.profile.model.ProfileTab
import com.broken.telephone.features.profile.use_case.GetCurrentUserUseCase
import com.broken.telephone.features.profile.use_case.GetMyContributionsUseCase
import com.broken.telephone.features.profile.use_case.GetMyPostsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMyPostsUseCase: GetMyPostsUseCase,
    private val getMyContributionsUseCase: GetMyContributionsUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val notInterestedUseCase: NotInterestedUseCase,
    private val reportPostUseCase: ReportPostUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<ProfileSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
        viewModelScope.launch {
            getMyPostsUseCase().collect { posts ->
                _state.update { it.copy(myPosts = posts) }
            }
        }
        viewModelScope.launch {
            getMyContributionsUseCase().collect { posts ->
                _state.update { it.copy(myContributions = posts) }
            }
        }
    }

    fun onTabSelect(tab: ProfileTab) {
        _state.update { it.copy(selectedTab = tab) }
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
            _sideEffects.send(ProfileSideEffect.ShowNotInterestedToast)
        }
    }

    fun onBlockClick() {
        _state.update { it.copy(isPostBottomSheetVisible = false, isBlockDialogVisible = true) }
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
        viewModelScope.launch { _sideEffects.send(ProfileSideEffect.ShowReportSuccessToast) }
    }
}
