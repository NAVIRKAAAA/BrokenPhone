package com.brokentelephone.game.features.user_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.post.PostUi
import com.brokentelephone.game.core.model.post.toUi
import com.brokentelephone.game.core.model.profile.ProfileTab
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.model.report.ReportUserType
import com.brokentelephone.game.domain.use_case.AcceptFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.BlockUserUseCase
import com.brokentelephone.game.domain.use_case.CancelFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetFriendsCountUseCase
import com.brokentelephone.game.domain.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.GetUserByIdUseCase
import com.brokentelephone.game.domain.use_case.GetUserCompletedContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserCompletedPostsUseCase
import com.brokentelephone.game.domain.use_case.GetUserLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.domain.use_case.ReportPostUseCase
import com.brokentelephone.game.domain.use_case.ReportUserUseCase
import com.brokentelephone.game.domain.use_case.SendFriendRequestUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.user_details.model.UserDetailsSideEffect
import com.brokentelephone.game.features.user_details.model.UserDetailsState
import com.brokentelephone.game.features.user_details.use_case.GetFriendshipActionStateUseCase
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

class UserDetailsViewModel(
    private val userId: String,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val getFriendshipActionStateUseCase: GetFriendshipActionStateUseCase,
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val acceptFriendRequestUseCase: AcceptFriendRequestUseCase,
    private val cancelFriendRequestUseCase: CancelFriendRequestUseCase,
    private val removeFriendUseCase: RemoveFriendUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val getUserLinkByIdUseCase: GetUserLinkByIdUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val markPostAsNotInterestedUseCase: MarkPostAsNotInterestedUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val reportUserUseCase: ReportUserUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val getFriendsCountUseCase: GetFriendsCountUseCase,
    private val getUserCompletedPostsUseCase: GetUserCompletedPostsUseCase,
    private val getUserCompletedContributionsUseCase: GetUserCompletedContributionsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserDetailsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<UserDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var lastLoadedAt: Long = 0L

    init {
        getCurrentUserUseCase.execute()
            .onEach { user -> _state.update { it.copy(currentUser = user?.toUi()) } }
            .launchIn(viewModelScope)
    }

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
            val friendsCount = async { fetchFriendsCount() }
            posts.await()
            contributions.await()
            friendsCount.await()
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
            val friendsCount = async { fetchFriendsCount() }
            posts.await()
            contributions.await()
            friendsCount.await()

            lastLoadedAt = System.currentTimeMillis()

            _state.update { it.copy(isRefreshing = false) }
        }
    }

    private suspend fun fetchFriendsCount() {
        getFriendsCountUseCase.execute(userId)
            .onSuccess { count -> _state.update { it.copy(friendsCount = count) } }
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

        getUserCompletedPostsUseCase.execute(user.id)
            .onSuccess { posts ->
                _state.update {
                    it.copy(
                        isPostsLoading = false,
                        myPosts = posts.map { p -> p.toUi() }
//                        myPosts = listOf()
                    )
                }
            }
            .onError {
                _state.update { it.copy(isPostsLoading = false) }
            }
    }

    private suspend fun fetchContributions() {
        val user = state.value.user ?: return

        getUserCompletedContributionsUseCase.execute(user.id)
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

    fun onAcceptRequestClick() {
        viewModelScope.launch {
            _state.update { it.copy(isFriendshipActionLoading = true) }
            acceptFriendRequestUseCase.execute(userId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isFriendshipActionLoading = false,
                            friendshipActionState = FriendshipActionState.FRIENDS,
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

    fun onCancelRequestClick() {
        _state.update { it.copy(isCancelRequestDialogVisible = true) }
    }

    fun onCancelRequestDialogDismiss() {
        _state.update { it.copy(isCancelRequestDialogVisible = false) }
    }

    fun onCancelRequestConfirm() {
        _state.update { it.copy(isCancelRequestLoading = true) }
        viewModelScope.launch {
            cancelFriendRequestUseCase.execute(userId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isCancelRequestLoading = false,
                            isCancelRequestDialogVisible = false,
                            friendshipActionState = FriendshipActionState.NOT_FRIENDS,
                        )
                    }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            isCancelRequestLoading = false,
                            isCancelRequestDialogVisible = false,
                            globalError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    fun onRemoveFriendClick() {
        _state.update { it.copy(isRemoveFriendDialogVisible = true) }
    }

    fun onRemoveFriendDialogDismiss() {
        _state.update { it.copy(isRemoveFriendDialogVisible = false) }
    }

    fun onRemoveFriendConfirm() {
        _state.update { it.copy(isRemoveFriendLoading = true) }
        viewModelScope.launch {
            removeFriendUseCase.execute(userId)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isRemoveFriendLoading = false,
                            isRemoveFriendDialogVisible = false,
                            friendshipActionState = FriendshipActionState.NOT_FRIENDS,
                        )
                    }
                }
                .onError { exception ->
                    _state.update {
                        it.copy(
                            isRemoveFriendLoading = false,
                            isRemoveFriendDialogVisible = false,
                            globalError = exceptionToMessageMapper.map(exception),
                        )
                    }
                }
        }
    }

    fun onCopyUserLinkClick() {
        val link = getUserLinkByIdUseCase.execute(userId)
        _state.update { it.copy(isUserBottomSheetVisible = false) }
        viewModelScope.launch { _sideEffects.send(UserDetailsSideEffect.ShowCopyLinkSuccessToast(link)) }
    }

    fun onMoreVertClick() {
        _state.update { it.copy(isUserBottomSheetVisible = true) }
    }

    fun onUserBottomSheetDismiss() {
        _state.update { it.copy(isUserBottomSheetVisible = false) }
    }

    fun onUserReportClick() {
        _state.update { it.copy(isUserBottomSheetVisible = false, isUserReportBottomSheetVisible = true) }
    }

    fun onUserReportBottomSheetDismiss() {
        _state.update { it.copy(isUserReportBottomSheetVisible = false) }
    }

    fun onUserReportTypeSelected(type: ReportUserType) {
        _state.update { it.copy(isUserReportBottomSheetVisible = false) }
        viewModelScope.launch {
            reportUserUseCase.execute(userId, type)
                .onSuccess {
                    _sideEffects.send(UserDetailsSideEffect.ShowReportSuccessToast)
                }.onError { exception ->
                    _state.update { it.copy(globalError = exceptionToMessageMapper.map(exception)) }
                }
        }
    }

    fun onMoreClick(post: PostUi) {
        _state.update { it.copy(selectedPost = post, isPostBottomSheetVisible = true) }
    }

    fun onPostBottomSheetDismiss() {
        _state.update { it.copy(isPostBottomSheetVisible = false) }
    }

    fun onCopyLinkClick() {
        val postId = state.value.selectedPost?.id ?: return
        val link = getPostLinkByIdUseCase.execute(postId)
        _state.update { it.copy(isPostBottomSheetVisible = false) }
        viewModelScope.launch { _sideEffects.send(UserDetailsSideEffect.ShowCopyLinkSuccessToast(link)) }
    }

    fun onNotInterestedClick() {
        val postId = state.value.selectedPost?.id ?: return
        _state.update { it.copy(isPostBottomSheetVisible = false, selectedPost = null) }
        viewModelScope.launch {
            markPostAsNotInterestedUseCase.execute(postId).onSuccess {
                _sideEffects.send(UserDetailsSideEffect.NavigateBackWithForceUpdate)
            }.onError { exception ->
                _state.update { it.copy(globalError = exceptionToMessageMapper.map(exception)) }
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
        _state.update { it.copy(isBlockLoading = true) }
        viewModelScope.launch {
            blockUserUseCase.execute(userId).onSuccess {
                _state.update {
                    it.copy(isBlockLoading = false, isBlockDialogVisible = false, selectedPost = null)
                }
                _sideEffects.send(UserDetailsSideEffect.NavigateBackWithForceUpdate)
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isBlockLoading = false,
                        isBlockDialogVisible = false,
                        selectedPost = null,
                        globalError = exceptionToMessageMapper.map(exception),
                    )
                }
            }
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
        viewModelScope.launch {
            reportPostUseCase.execute(postId, type).onSuccess {
                _sideEffects.send(UserDetailsSideEffect.ShowReportSuccessToast)
            }.onError { exception ->
                _state.update { it.copy(globalError = exceptionToMessageMapper.map(exception)) }
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
        val post = state.value.selectedPost ?: return
        _state.update { it.copy(isDeleteLoading = true) }
        viewModelScope.launch {
            deletePostUseCase.execute(post.id).onSuccess {
                _state.update {
                    it.copy(isDeleteLoading = false, isDeleteDialogVisible = false, selectedPost = null)
                }
                onRefresh()
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isDeleteLoading = false,
                        isDeleteDialogVisible = false,
                        selectedPost = null,
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
