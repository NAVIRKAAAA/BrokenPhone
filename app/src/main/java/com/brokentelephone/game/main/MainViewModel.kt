package com.brokentelephone.game.main

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.session.GameSessionStatus
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.app_preferences.use_case.GetLanguageUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetThemeUseCase
import com.brokentelephone.game.features.language.use_case.InitializeLanguageUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.main.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import com.brokentelephone.game.navigation.routes.Routes
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel(
    private val getThemeUseCase: GetThemeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val initializeLanguageUseCase: InitializeLanguageUseCase,
    private val initializeSessionUseCase: InitializeSessionUseCase,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val applyEmailChangeUseCase: ApplyEmailChangeUseCase,
    private val getPendingEmailUseCase: GetPendingEmailUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<MainSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var bannerTimerJob: Job? = null

    init {
        observeTheme()
        observeLanguage()
        initializeSession()
    }

    private fun observeTheme() {
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }

    private fun observeLanguage() {
        getLanguageUseCase()
            .onEach { language -> _state.update { it.copy(language = language) } }
            .launchIn(viewModelScope)
    }

    fun initializeSession() {
        _state.update { it.copy(isSessionLoading = true) }
        viewModelScope.launch {
            initializeSessionUseCase.execute().onSuccess { authState ->
                val user = authState.getUserOrNull()

                val (destination, pendingRoutes) = when (user?.onboardingStep) {
                    null -> Routes.Welcome to emptyList()
                    OnboardingStep.CHOOSE_AVATAR -> Routes.ChooseAvatar to emptyList()
                    OnboardingStep.CHOOSE_USERNAME -> Routes.ChooseAvatar to listOf(Routes.ChooseUsername)
                    OnboardingStep.COMPLETED -> Routes.Dashboard to emptyList()
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
                        startDestination = Routes.Welcome
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
    }

    private suspend fun setActiveSession(sessionId: String) {
        getActiveSessionUseCase.execute(sessionId).onSuccess { session ->

            delay(300)

            _state.update { it.copy(activeSession = session) }
            if (session.status == GameSessionStatus.ACTIVE) {
                startBannerTimer(session)
            } else {
                bannerTimerJob?.cancel()
                _state.update { it.copy(bannerRemainingSeconds = 0) }
            }
        }
    }

    private fun startBannerTimer(session: GameSession) {
        bannerTimerJob?.cancel()
        bannerTimerJob = viewModelScope.launch {
            while (isActive) {
                val remaining = ((session.expiresAt - System.currentTimeMillis()) / 1000).toInt()
                _state.update { it.copy(bannerRemainingSeconds = remaining.coerceAtLeast(0)) }
                if (remaining <= 0) break
                delay(1000)
            }
        }
    }

    fun onBannerDismissed() {
        bannerTimerJob?.cancel()
        _state.update {
            it.copy(
                activeSession = null,
                bannerRemainingSeconds = 0
            )
        }
    }

    fun onBannerContinueClick() {
        val session = _state.value.activeSession ?: return
        _state.update { it.copy(isBannerLoading = true) }
        viewModelScope.launch {
            getPostByIdUseCase.executeWithResult(session.postId).onSuccess { post ->
                val effect = when (post.content) {
                    is PostContent.Text -> MainSideEffect.NavigateToDraw(Routes.Draw(session.id))
                    is PostContent.Drawing -> MainSideEffect.NavigateToDescribeDrawing(
                        Routes.DescribeDrawing(session.id)
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

    fun onSessionErrorDismissed() {
        _state.update { it.copy(sessionDataError = null, startDestination = Routes.Welcome) }
    }

    fun onPendingRoutesConsumed() {
        _state.update { it.copy(pendingRoutes = emptyList()) }
    }

    fun initializeDefaultLanguage(deviceLanguageTag: String) {
        viewModelScope.launch {
            initializeLanguageUseCase(deviceLanguageTag)
        }
    }

    fun handleEmailChangeLink(uri: Uri) {
        Log.d("LOG_TAG", "handleEmailChangeLink: $uri")
        val innerLink = uri.getQueryParameter("link") ?: return
        val innerUri = innerLink.toUri()
        val oobCode = innerUri.getQueryParameter("oobCode") ?: return
        if (innerUri.getQueryParameter("mode") != "verifyAndChangeEmail") return

        Log.d("LOG_TAG", "oobCode: $oobCode")

        viewModelScope.launch {
            _state.update { it.copy(isEmailChanging = true) }
            applyEmailChangeUseCase.execute(oobCode)
            val pendingEmail = getPendingEmailUseCase.execute() ?: ""
            _state.update { it.copy(isEmailChanging = false) }
            _sideEffects.send(MainSideEffect.NavigateToSignIn(pendingEmail))
        }
    }
}
