package com.brokentelephone.game.features.choose_username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameEvent
import com.brokentelephone.game.features.choose_username.model.ChooseUsernameState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChooseUsernameViewModel : ViewModel() {

    private val _state = MutableStateFlow(ChooseUsernameState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ChooseUsernameEvent>()
    val event = _event.asSharedFlow()

    fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username) }
    }

    fun onContinueClick() {
        viewModelScope.launch {
            _event.emit(ChooseUsernameEvent.NavigateNext(_state.value.username))
        }
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
