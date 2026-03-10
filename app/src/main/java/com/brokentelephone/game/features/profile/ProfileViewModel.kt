package com.brokentelephone.game.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.bottom_sheet.report_post_bottom_sheet.model.ReportPostType
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.post_details.use_case.BlockUserUseCase
import com.brokentelephone.game.features.post_details.use_case.DeletePostUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.NotInterestedUseCase
import com.brokentelephone.game.features.post_details.use_case.ReportPostUseCase
import com.brokentelephone.game.features.profile.model.ProfileSideEffect
import com.brokentelephone.game.features.profile.model.ProfileState
import com.brokentelephone.game.features.profile.model.ProfileTab
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.features.profile.use_case.GetMyContributionsUseCase
import com.brokentelephone.game.features.profile.use_case.GetMyPostsUseCase
import com.brokentelephone.game.features.settings.use_case.GetAuthStateUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMyPostsUseCase: GetMyPostsUseCase,
    private val getMyContributionsUseCase: GetMyContributionsUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val notInterestedUseCase: NotInterestedUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<ProfileSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getAuthStateUseCase()
            .onEach { authState -> _state.update { it.copy(isAuth = authState.isAuth()) } }
            .launchIn(viewModelScope)

        val userFlow = getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user) } }

        userFlow
            .map { it?.id }
            .filterNotNull()
            .flatMapLatest { userId -> getMyPostsUseCase(userId) }
            .onEach { posts -> _state.update { it.copy(myPosts = posts, isLoading = false) } }
            .launchIn(viewModelScope)

        userFlow
            .map { it?.id }
            .filterNotNull()
            .flatMapLatest { userId -> getMyContributionsUseCase(userId) }
            .onEach { posts -> _state.update { it.copy(myContributions = posts) } }
            .launchIn(viewModelScope)
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
        viewModelScope.launch { _sideEffects.send(ProfileSideEffect.ShowReportSuccessToast) }
    }
}
