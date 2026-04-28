package com.brokentelephone.game.features.edit_username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.edit_username.model.EditUsernameEvent
import com.brokentelephone.game.features.edit_username.model.EditUsernameState
import com.brokentelephone.game.features.edit_username.use_case.UpdateUsernameUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditUsernameViewModel(
    private val updateUsernameUseCase: UpdateUsernameUseCase,
    private val userSession: UserSession,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(EditUsernameState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditUsernameEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            val user = userSession.user.firstOrNull() ?: return@launch
            _state.update { it.copy(username = user.username, initialUsername = user.username) }
        }
    }

    fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    fun onSave() {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updateUsernameUseCase.execute(_state.value.username).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _event.emit(EditUsernameEvent.NavigateBack)
            }.onError { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        globalError = exceptionToMessageMapper.map(error)
                    )
                }
            }
        }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    companion object {
        const val MAX_USERNAME_LENGTH = 20
    }
}
