package com.brokentelephone.game.main.activity

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarRoute
import com.brokentelephone.game.choose_username_api.ChooseUsernameRoute
import com.brokentelephone.game.dashboard_api.MainGraph
import com.brokentelephone.game.describe_drawing_api.DescribeDrawingRoute
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.banner.BannerType
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSessionStatus
import com.brokentelephone.game.domain.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.domain.use_case.GetPostByIdUseCase
import com.brokentelephone.game.domain.use_case.GetThemeUseCase
import com.brokentelephone.game.domain.use_case.HideBannerUseCase
import com.brokentelephone.game.domain.use_case.ObserveBannerUseCase
import com.brokentelephone.game.domain.use_case.ShowBannerUseCase
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.draw_api.DrawRoute
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.language.use_case.InitializeFirstAppLaunchUseCase
import com.brokentelephone.game.features.notifications_settings.use_case.UpdateUserPermissionsUseCase
import com.brokentelephone.game.main.activity.model.MainSideEffect
import com.brokentelephone.game.main.activity.model.MainState
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailVerificationUseCase
import com.brokentelephone.game.main.use_case.ApplyPasswordResetUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import com.brokentelephone.game.navigation.nav_graph.AuthGraph
import com.brokentelephone.game.notification_details_api.NotificationDetailsRoute
import com.brokentelephone.game.notifications_api.NotificationsRoute
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MainViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val initializeFirstAppLaunchUseCase: InitializeFirstAppLaunchUseCase,
    private val initializeSessionUseCase: InitializeSessionUseCase,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val applyEmailChangeUseCase: ApplyEmailChangeUseCase,
    private val applyEmailVerificationUseCase: ApplyEmailVerificationUseCase,
    private val applyPasswordResetUseCase: ApplyPasswordResetUseCase,
    private val updateUserPermissionsUseCase: UpdateUserPermissionsUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val observeBannerUseCase: ObserveBannerUseCase,
    private val showBannerUseCase: ShowBannerUseCase,
    private val hideBannerUseCase: HideBannerUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<MainSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        observeTheme()
        observeBanner()
        initializeSession()
        initializeFirstLaunch()
    }

    private fun observeBanner() {
        observeBannerUseCase.execute()
            .onEach { banner -> _state.update { it.copy(currentBanner = banner) } }
            .launchIn(viewModelScope)
    }

    fun onResume(isNotificationsGranted: Boolean) {
        viewModelScope.launch {
            updateUserPermissionsUseCase.execute(UserPermissions(isNotificationsGranted = isNotificationsGranted))
        }
    }

    private fun initializeFirstLaunch() {
        viewModelScope.launch {
            initializeFirstAppLaunchUseCase.execute()
        }
    }

    private fun observeTheme() {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }

    // 500 ms to start
    fun initializeSession() {
        _state.update { it.copy(isSessionLoading = true) }

        viewModelScope.launch {
            val time = measureTimeMillis {

                initializeSessionUseCase.execute().onSuccess { authState ->
                    val user = authState.getUserOrNull()

                    val (destination, pendingRoutes) = when (user?.onboardingStep) {
                        OnboardingStep.CHOOSE_AVATAR -> AuthGraph to listOf(
                            ChooseAvatarRoute
                        )

                        OnboardingStep.CHOOSE_USERNAME -> AuthGraph to listOf(
                            ChooseAvatarRoute,
                            ChooseUsernameRoute
                        )

                        OnboardingStep.COMPLETED -> {
                            val notificationRoutes = _state.value.pendingNotificationId
                                ?.let { listOf(NotificationsRoute, NotificationDetailsRoute(it)) }
                                ?: emptyList()
                            _state.update { it.copy(pendingNotificationId = null) }
                            MainGraph to notificationRoutes
                        }

                        null -> AuthGraph to emptyList()
                    }

                    _state.update {
                        it.copy(
                            sessionDataError = null,
                            isSessionLoading = false,
                            startDestination = destination,
                            pendingRoutes = pendingRoutes
                        )
                    }

                    val sessionId = user?.sessionId

                    if (user != null && user.onboardingStep == OnboardingStep.COMPLETED && sessionId != null) {
                        setActiveSession(sessionId)
                    }
                }.onError { error ->
                    _state.update {
                        it.copy(
                            isSessionLoading = false,
                            startDestination = AuthGraph
                        )
                    }

                    if (error is SessionDataException) {
                        delay(150)
                        _state.update {
                            it.copy(
                                isSessionLoading = false,
                                sessionDataError = exceptionToMessageMapper.map(error)
                            )
                        }
                    }
                }
            }
            Log.d("LOG_TAG", "initializeSession: $time")

        }
    }

    private suspend fun setActiveSession(sessionId: String) {
        getActiveSessionUseCase.execute(sessionId).onSuccess { session ->
            if (session.status == GameSessionStatus.ACTIVE) {

                delay(300)

                showBannerUseCase.execute(
                    BannerType.ActiveSession(
                        id = session.id,
                        postId = session.postId,
                        expiresAt = session.expiresAt,
                        remainingSeconds = ((session.expiresAt - System.currentTimeMillis()) / 1000).toInt()
                            .coerceAtLeast(0),
                        totalSeconds = ((session.expiresAt - session.lockedAt) / 1000).toInt(),
                    )
                )
            }
        }
    }

    fun onBannerDismissed() {
        hideBannerUseCase.execute()
        _state.update { it.copy(isBannerLoading = false) }
    }

    fun onBannerContinueClick() {
        viewModelScope.launch {
            _state.update { it.copy(isBannerLoading = true) }
            when (val banner = _state.value.currentBanner ?: return@launch) {
                is BannerType.ActiveSession -> {
                    getPostByIdUseCase.executeWithResult(banner.postId).onSuccess { post ->
                        val effect = when (post.content) {
                            is PostContent.Text -> MainSideEffect.NavigateToDraw(DrawRoute(banner.id))
                            is PostContent.Drawing -> MainSideEffect.NavigateToDescribeDrawing(
                                DescribeDrawingRoute(banner.id)
                            )
                        }
                        _sideEffects.send(effect)

                        delay(150)

                        onBannerDismissed()
                    }.onError {
                        _state.update { it.copy(isBannerLoading = false) }
                    }
                }
            }
        }
    }

    fun onSessionErrorDismissed() {
        _state.update { it.copy(sessionDataError = null, startDestination = AuthGraph) }
    }

    fun onPendingRoutesConsumed() {
        _state.update { it.copy(pendingRoutes = emptyList()) }
    }

    // TODO: to handleNewIntent
    fun handleFcmTapWhileRunning(notificationId: String) {
        viewModelScope.launch {
            Log.d("LOG_TAG", "handleFcmTapWhileRunning()")
            _sideEffects.send(MainSideEffect.NavigateToNotificationDetails(notificationId))
        }
    }

    // TODO: to handleNewIntent
    fun handleFcmTap(notificationId: String) {
        Log.d("LOG_TAG", "handleFcmTap(): $notificationId")
        _state.update { it.copy(pendingNotificationId = notificationId) }
    }

    fun handleNewIntent(uri: Uri) {
        Log.d("LOG_TAG", "handleNewIntent: $uri")

        if (uri.host == "reset-password") {
            handlePasswordReset(uri)
            return
        }

        if (uri.host == "change-email") {
            val code = uri.getQueryParameter("code") ?: return
            handleEmailChange(code)
            return
        }

        if (uri.host == "verify-email") {
            val code = uri.getQueryParameter("code") ?: return
            handleEmailVerification(code)
            return
        }
    }

    private fun handlePasswordReset(uri: Uri) {
        val code = uri.getQueryParameter("code") ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            applyPasswordResetUseCase.execute(code)
                .onSuccess {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffects.send(MainSideEffect.NavigateToNewPassword)
                }
                .onError { e ->
                    Log.d("LOG_TAG", "handlePasswordReset: $e")
                    _state.update { it.copy(isLoading = false) }
                }
        }
    }

    private fun handleEmailVerification(code: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            applyEmailVerificationUseCase.execute(code)
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handleEmailChange(oobCode: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            applyEmailChangeUseCase.execute(oobCode)
            _state.update { it.copy(isLoading = false) }
        }
    }
}
