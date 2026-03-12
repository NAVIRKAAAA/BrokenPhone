package com.brokentelephone.game.features.choose_username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.handler.onError
import com.brokentelephone.game.domain.handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameEvent
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameState
import com.brokentelephone.game.features.choose_username.use_case.CompleteUsernameStepUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChooseUsernameViewModel(
    private val completeUsernameStepUseCase: CompleteUsernameStepUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(ChooseUsernameState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ChooseUsernameEvent>()
    val event = _event.asSharedFlow()

    fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    fun onContinueClick() {
        if (_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            completeUsernameStepUseCase.execute(_state.value.username).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _event.emit(ChooseUsernameEvent.NavigateNext)
            }.onError { error ->
                _state.update { it.copy(isLoading = false, globalError = exceptionToMessageMapper.map(error)) }
            }
        }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onBack() {
        viewModelScope.launch {
            _event.emit(ChooseUsernameEvent.NavigateBack)
        }
    }

    companion object {
        const val MAX_USERNAME_LENGTH = 20
    }
}
