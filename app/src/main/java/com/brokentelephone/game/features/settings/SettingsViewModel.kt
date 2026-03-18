package com.brokentelephone.game.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.app_preferences.use_case.GetLanguageUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetThemeUseCase
import com.brokentelephone.game.features.settings.model.SettingsSideEffect
import com.brokentelephone.game.features.settings.model.SettingsState
import com.brokentelephone.game.features.settings.use_case.GetAuthStateUseCase
import com.brokentelephone.game.features.settings.use_case.GetBlockedUsersCountUseCase
import com.brokentelephone.game.features.settings.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.features.settings.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.features.settings.use_case.GetVersionInfoUseCase
import com.brokentelephone.game.features.settings.use_case.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
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
    private val getBlockedUsersCountUseCase: GetBlockedUsersCountUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<SettingsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        getBlockedUsersCountUseCase()
            .onEach { count -> _state.update { it.copy(blockedUsersCount = count) } }
            .launchIn(viewModelScope)

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
