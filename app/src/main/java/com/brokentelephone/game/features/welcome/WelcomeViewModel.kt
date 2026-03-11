package com.brokentelephone.game.features.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.handler.onError
import com.brokentelephone.game.domain.handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.welcome.model.WelcomeSideEffect
import com.brokentelephone.game.features.welcome.model.WelcomeState
import com.brokentelephone.game.features.welcome.use_case.SignInAnonymouslyUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val signInAnonymouslyUseCase: SignInAnonymouslyUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<WelcomeSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onPlayAsGuestClick() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            signInAnonymouslyUseCase.execute().onSuccess {
                _state.update { it.copy(isLoading = false) }
                _sideEffects.send(WelcomeSideEffect.NavigateToDashboard)
            }.onError { error ->
                val message = exceptionToMessageMapper.map(error)
                _state.update { it.copy(isLoading = false, globalError = message) }
            }
        }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }
}
