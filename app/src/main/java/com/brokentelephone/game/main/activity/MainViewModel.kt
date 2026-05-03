package com.brokentelephone.game.main.activity

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.draw_api.DrawRoute
import com.brokentelephone.game.features.notifications_settings.use_case.UpdateUserPermissionsUseCase
import com.brokentelephone.game.main.activity.model.MainSideEffect
import com.brokentelephone.game.main.activity.model.MainSideEffect.NavigateToDescribeDrawing
import com.brokentelephone.game.main.activity.model.MainSideEffect.NavigateToDraw
import com.brokentelephone.game.main.activity.model.MainSideEffect.NavigateToNewPassword
import com.brokentelephone.game.main.activity.model.MainSideEffect.NavigateToNotificationDetails
import com.brokentelephone.game.main.activity.model.MainState
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailVerificationUseCase
import com.brokentelephone.game.main.use_case.ApplyPasswordResetUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import com.brokentelephone.game.main.use_case.ObserveNewNotificationsUseCase
import com.brokentelephone.game.navigation.nav_graph.AuthGraph
import com.brokentelephone.game.notification_details_api.NotificationDetailsRoute
import com.brokentelephone.game.notifications_api.NotificationsRoute
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val initializeSessionUseCase: InitializeSessionUseCase,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val applyEmailChangeUseCase: ApplyEmailChangeUseCase,
    private val applyEmailVerificationUseCase: ApplyEmailVerificationUseCase,
    private val applyPasswordResetUseCase: ApplyPasswordResetUseCase,
    private val updateUserPermissionsUseCase: UpdateUserPermissionsUseCase,
    private val observeBannerUseCase: ObserveBannerUseCase,
    private val showBannerUseCase: ShowBannerUseCase,
    private val hideBannerUseCase: HideBannerUseCase,
    private val observeNewNotificationsUseCase: ObserveNewNotificationsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<MainSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private val sessionStartTime = System.currentTimeMillis()

    init {
        Log.d("LOG_TAG", "MainViewModel Init")
        initializeSession()
        observeTheme()
        observeBanner()
        startNotificationObserver()
    }

    private fun startNotificationObserver() {
        viewModelScope.launch {
            observeNewNotificationsUseCase.execute()
        }
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

    private fun observeTheme() {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }

    fun initializeSession() {
        viewModelScope.launch {
            initializeSessionUseCase.execute().onSuccess { authStateAsFlow ->
                authStateAsFlow.collect { authState ->
                    Log.d("LOG_TAG", "initializeSession: $authState")

                    when (authState) {
                        is AuthState.Auth -> {
                            val user = authState.user

                            when (user.onboardingStep) {
                                OnboardingStep.CHOOSE_AVATAR -> {
                                    _sideEffects.send(MainSideEffect.NavigateToChooseAvatar)
                                }

                                OnboardingStep.CHOOSE_USERNAME -> {
                                    _sideEffects.send(MainSideEffect.NavigateToChooseUsername)
                                }

                                OnboardingStep.COMPLETED -> {
                                    val sessionId = user.sessionId
                                    if (sessionId != null) {
                                        setActiveSession(sessionId)
                                    }
                                }
                            }

                            cancel()
                        }

                        is AuthState.PreAuth -> {
                            val pendingRoutes = _state.value.pendingNotificationId
                                ?.let { listOf(NotificationsRoute, NotificationDetailsRoute(it)) }
                                ?: emptyList()
                            _state.update { it.copy(pendingNotificationId = null) }

                            Log.d("LOG_TAG", "initializeSession ${System.currentTimeMillis() - sessionStartTime}ms")

                            Log.d("LOG_TAG", "Setup MainGraph as startDestination")
                            _state.update {
                                it.copy(
                                    startDestination = MainGraph,
                                    pendingRoutes = pendingRoutes
                                )
                            }
                        }

                        AuthState.NotAuth -> {
                            _state.update {
                                it.copy(
                                    startDestination = AuthGraph,
                                    pendingRoutes = emptyList()
                                )
                            }

                            cancel()
                        }

                        is AuthState.Loading -> {
                            // Just waiting for new auth event
                            return@collect
                        }
                    }
                }
            }.onError {
                _state.update {
                    it.copy(
                        startDestination = AuthGraph,
                        pendingRoutes = emptyList()
                    )
                }
            }
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
                            is PostContent.Text -> NavigateToDraw(DrawRoute(banner.id))
                            is PostContent.Drawing -> NavigateToDescribeDrawing(
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

                is BannerType.NewsNotification -> {
                    _sideEffects.send(NavigateToNotificationDetails(banner.id))
                    onBannerDismissed()
                }
            }
        }
    }

    fun onPendingRoutesConsumed() {
        _state.update { it.copy(pendingRoutes = emptyList()) }
    }

    // TODO: to handleNewIntent
    fun handleFcmTapWhileRunning(notificationId: String) {
        viewModelScope.launch {
            Log.d("LOG_TAG", "handleFcmTapWhileRunning()")
            _sideEffects.send(NavigateToNotificationDetails(notificationId))
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
                    _sideEffects.send(NavigateToNewPassword)
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
