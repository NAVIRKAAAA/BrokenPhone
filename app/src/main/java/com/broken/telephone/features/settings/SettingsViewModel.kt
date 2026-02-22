package com.broken.telephone.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.features.settings.model.SettingsSideEffect
import com.broken.telephone.features.settings.model.SettingsState
import com.broken.telephone.features.settings.use_case.GetVersionInfoUseCase
import com.broken.telephone.features.settings.use_case.LogoutUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val getVersionInfoUseCase: GetVersionInfoUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<SettingsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        _state.update { it.copy(versionInfo = getVersionInfoUseCase()) }
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
            logoutUseCase()
            _state.update { it.copy(isLogoutLoading = false, isLogoutDialogVisible = false) }
            _sideEffects.send(SettingsSideEffect.NavigateToWelcome)
        }
    }
}
