package com.brokentelephone.game.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.model.user.toUi
import com.brokentelephone.game.describe_drawing_api.DescribeDrawingRoute
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.session.GameSessionStatus
import com.brokentelephone.game.domain.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.domain.use_case.GetAuthStateUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetLanguageUseCase
import com.brokentelephone.game.domain.use_case.GetPostByIdUseCase
import com.brokentelephone.game.domain.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.domain.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.domain.use_case.GetThemeUseCase
import com.brokentelephone.game.domain.use_case.LogoutUseCase
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.draw_api.DrawRoute
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.settings.model.SettingsSideEffect
import com.brokentelephone.game.features.settings.model.SettingsState
import com.brokentelephone.game.features.settings.use_case.GetVersionInfoUseCase
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

class SettingsViewModel(
    private val getVersionInfoUseCase: GetVersionInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val getAuthStateUseCase: GetAuthStateUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getTermsOfServiceLinkUseCase: GetTermsOfServiceLinkUseCase,
    private val getPrivacyPolicyLinkUseCase: GetPrivacyPolicyLinkUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getActiveSessionUseCase: GetActiveSessionUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<SettingsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    private var sessionTimerJob: Job? = null

    init {
        observeCurrentUser()
        _state.update { it.copy(versionInfo = getVersionInfoUseCase()) }
        getAuthStateUseCase()
            .onEach { authState -> _state.update { it.copy(isAuth = authState.isAuth() || authState is AuthState.Guest) } }
            .launchIn(viewModelScope)
        getLanguageUseCase()
            .onEach { language -> _state.update { it.copy(language = language) } }
            .launchIn(viewModelScope)
        getThemeUseCase()
            .onEach { theme -> _state.update { it.copy(theme = theme) } }
            .launchIn(viewModelScope)
    }

    private fun observeCurrentUser() {
        getCurrentUserUseCase()
            .onEach { user ->
                _state.update { it.copy(user = user?.toUi()) }
                val sessionId = user?.sessionId
                if (sessionId != null) {
                    loadActiveSession(sessionId)
                } else {
                    sessionTimerJob?.cancel()
                    _state.update { it.copy(sessionRemainingSeconds = 0) }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadActiveSession(sessionId: String) {
        viewModelScope.launch {
            getActiveSessionUseCase.execute(sessionId).onSuccess { session ->
                _state.update { it.copy(session = session) }

                if (session.status == GameSessionStatus.ACTIVE) {
                    startSessionTimer(session)
                } else {
                    sessionTimerJob?.cancel()
                    _state.update { it.copy(sessionRemainingSeconds = 0) }
                }
            }
        }
    }

    private fun startSessionTimer(session: GameSession) {
        sessionTimerJob?.cancel()
        sessionTimerJob = viewModelScope.launch {
            while (isActive) {
                val remaining = ((session.expiresAt - System.currentTimeMillis()) / 1000).toInt()
                _state.update { it.copy(sessionRemainingSeconds = remaining.coerceAtLeast(0)) }
                if (remaining <= 0) break
                delay(1000)
            }
        }
    }

    fun onTermsOfServiceClick() {
        viewModelScope.launch {
            _sideEffects.send(SettingsSideEffect.OpenLink(getTermsOfServiceLinkUseCase()))
        }
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _sideEffects.send(SettingsSideEffect.OpenLink(getPrivacyPolicyLinkUseCase()))
        }
    }

    fun checkNotificationsPermission(isGranted: Boolean) {
        _state.update { it.copy(notificationsEnabled = isGranted) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onActiveSessionClick() {
        val session = state.value.session ?: return
        _state.update { it.copy(isSessionLoading = true) }
        viewModelScope.launch {
            getPostByIdUseCase.executeWithResult(session.postId).onSuccess { post ->
                val effect = when (post.content) {
                    is PostContent.Text -> SettingsSideEffect.NavigateToDraw(DrawRoute(session.id))
                    is PostContent.Drawing -> SettingsSideEffect.NavigateToDescribeDrawing(DescribeDrawingRoute(session.id))
                }
                _sideEffects.send(effect)
            }.onError { error ->
                _state.update { it.copy(globalError = exceptionToMessageMapper.map(error)) }
            }
            _state.update { it.copy(isSessionLoading = false) }
        }
    }

    fun onLogoutClick() {
        _state.update { it.copy(isLogoutDialogVisible = true) }
    }

    fun onLogoutDismiss() {
        _state.update { it.copy(isLogoutDialogVisible = false) }
    }

    fun onLogoutConfirm() {
        _state.update { it.copy(isLogoutLoading = true) }
        viewModelScope.launch {
            logoutUseCase.execute().onSuccess {
                _state.update { it.copy(isLogoutLoading = false, isLogoutDialogVisible = false) }
                _sideEffects.send(SettingsSideEffect.NavigateToWelcome)
            }.onError { error ->
                val message = exceptionToMessageMapper.map(error)
                _state.update {
                    it.copy(
                        isLogoutLoading = false,
                        isLogoutDialogVisible = false,
                        globalError = message
                    )
                }
            }
        }
    }
}
