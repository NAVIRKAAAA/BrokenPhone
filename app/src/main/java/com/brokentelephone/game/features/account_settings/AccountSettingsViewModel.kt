package com.brokentelephone.game.features.account_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.features.account_settings.model.AccountSettingsSideEffect
import com.brokentelephone.game.features.account_settings.model.AccountSettingsState
import com.brokentelephone.game.features.account_settings.use_case.DeleteAccountUseCase
import com.brokentelephone.game.features.profile.model.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountSettingsViewModel(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AccountSettingsState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<AccountSettingsSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {

        getCurrentUserUseCase()
            .onEach { user -> _state.update { it.copy(user = user?.toUi()) } }
            .launchIn(viewModelScope)
    }

    fun onDeleteAccountClick() {
        _state.update { it.copy(isDeleteAccountDialogVisible = true) }
    }

    fun onDeleteAccountDismiss() {
        _state.update { it.copy(isDeleteAccountDialogVisible = false) }
    }

    fun onDeleteAccountConfirm() {
        _state.update { it.copy(isDeleteAccountLoading = true) }
        viewModelScope.launch {
            deleteAccountUseCase()
            _state.update { it.copy(isDeleteAccountLoading = false, isDeleteAccountDialogVisible = false) }
            _sideEffects.send(AccountSettingsSideEffect.NavigateToWelcome)
        }
    }
}
