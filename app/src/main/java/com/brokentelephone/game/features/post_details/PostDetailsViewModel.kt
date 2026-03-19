package com.brokentelephone.game.features.post_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.report.ReportPostType
import com.brokentelephone.game.domain.model.session.cooldownRemainingMs
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.dashboard.model.PostUi
import com.brokentelephone.game.features.post_details.model.PostDetailsSideEffect
import com.brokentelephone.game.features.post_details.model.PostDetailsState
import com.brokentelephone.game.features.post_details.use_case.BlockUserUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.JoinSessionUseCase
import com.brokentelephone.game.features.post_details.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.features.post_details.use_case.ReportPostUseCase
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostDetailsViewModel(
    private val postId: String,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val getPostLinkByIdUseCase: GetPostLinkByIdUseCase,
    private val reportPostUseCase: ReportPostUseCase,
    private val blockUserUseCase: BlockUserUseCase,
    private val markPostAsNotInterestedUseCase: MarkPostAsNotInterestedUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val joinSessionUseCase: JoinSessionUseCase,
    private val countdownTimer: CountdownTimer,
) : ViewModel() {

    private val _state = MutableStateFlow(PostDetailsState())
    val state = _state.asStateFlow()

    private var cooldownTimerJob: Job? = null

    private val _sideEffects = Channel<PostDetailsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getCurrentUserUseCase()
            .onEach { user ->
                _state.update { it.copy(userUi = user) }
            }
            .launchIn(viewModelScope)

        loadPost()
    }

    private fun startCooldownTimer(remainingSeconds: Int) {
        cooldownTimerJob?.cancel()
        cooldownTimerJob = countdownTimer.start(remainingSeconds)
            .onEach { remaining ->
                _state.update { it.copy(cooldownRemainingSeconds = remaining) }
            }
            .launchIn(viewModelScope)
    }

    private fun updateCooldown(postUi: PostUi) {
        val userId = _state.value.userUi?.id ?: return
        val remaining = (postUi.sessionsHistory.cooldownRemainingMs(userId) / 1000).toInt()
        if (remaining > 0) {
            startCooldownTimer(remaining)
        } else {
            cooldownTimerJob?.cancel()
            _state.update { it.copy(cooldownRemainingSeconds = 0) }
        }
    }

    private fun loadPost() {
        getPostByIdUseCase(postId)
            .onEach { postUi ->
                delay(150)
                _state.update {
                    it.copy(
                        postUi = postUi,
                        globalError = null,
                        globalException = null,
                        isLoadRetrying = false
                    )
                }
                updateCooldown(postUi)
            }
            .catch { e ->

                delay(150)

                _state.update {
                    it.copy(
                        globalError = exceptionToMessageMapper.map(e as Exception),
                        globalException = e,
                        isLoadRetrying = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onLoadErrorRetry() {
        _state.update { it.copy(isLoadRetrying = true) }
        loadPost()
    }

    fun onGlobalErrorDismiss() {
        val exception = state.value.globalException
        _state.update { it.copy(globalError = null, globalException = null) }

        if (exception is PostNotFoundException) {
            viewModelScope.launch {
                _sideEffects.send(PostDetailsSideEffect.NavigateBack)
            }
        }
    }

    fun onCopyLinkClick() {
        val link = getPostLinkByIdUseCase(postId)
        _state.update { it.copy(isBottomSheetVisible = false) }
        viewModelScope.launch {
            _sideEffects.send(
                PostDetailsSideEffect.ShowCopyLinkSuccessToast(
                    link
                )
            )
        }
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
//        val post = state.value.postUi ?: return
//        val postParentId = post.parentId
//        _state.update { it.copy(isBottomSheetVisible = false) }
//
//        viewModelScope.launch {
//            markPostAsNotInterestedUseCase.execute(postParentId).onSuccess {
//                _sideEffects.send(PostDetailsSideEffect.NavigateBackWithForceUpdate)
//            }.onError { exception ->
//                _state.update {
//                    it.copy(globalError = exceptionToMessageMapper.map(exception))
//                }
//            }
//        }
    }

    fun onBlockConfirm() {
        val userId = state.value.postUi?.authorId ?: return
        _state.update { it.copy(isBlockLoading = true) }

        viewModelScope.launch {
            blockUserUseCase.execute(userId).onSuccess {
                _state.update { it.copy(isBlockLoading = false, isBlockDialogVisible = false) }
                _sideEffects.send(PostDetailsSideEffect.NavigateBackWithForceUpdate)
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isBlockLoading = false,
                        isBlockDialogVisible = false,
                        globalError = exceptionToMessageMapper.map(exception)
                    )
                }
            }
        }
    }

    fun onContinueClick() {
        val post = state.value.postUi ?: return
        _state.update { it.copy(isContinueLoading = true) }
        viewModelScope.launch {
            joinSessionUseCase.execute(postId, post.nextTimeLimit).onSuccess {
                _state.update { it.copy(isContinueLoading = false) }
                val effect = when (post.content) {
                    is PostContent.Text -> PostDetailsSideEffect.NavigateToDraw(postId)
                    is PostContent.Drawing -> PostDetailsSideEffect.NavigateToDescribeDrawing(postId)
                }
                _sideEffects.send(effect)
            }.onError { exception ->
                _state.update {
                    it.copy(
                        isContinueLoading = false,
                        globalError = exceptionToMessageMapper.map(exception)
                    )
                }
            }
        }
    }

    fun onReportBottomSheetDismiss() {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
    }

    fun onReportTypeSelected(type: ReportPostType) {
        _state.update { it.copy(isReportBottomSheetVisible = false) }
        viewModelScope.launch {
            reportPostUseCase.execute(postId, type)
                .onSuccess {
                    _sideEffects.send(PostDetailsSideEffect.ShowReportSuccessToast)
                }.onError { exception ->
                    _state.update { it.copy(globalError = exceptionToMessageMapper.map(exception)) }
                }
        }
    }

}
