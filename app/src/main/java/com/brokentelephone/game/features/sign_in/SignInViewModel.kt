package com.brokentelephone.game.features.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.handler.onError
import com.brokentelephone.game.domain.handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.auth.InvalidCredentialsException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.sign_in.model.SignInSideEffect
import com.brokentelephone.game.features.sign_in.model.SignInState
import com.brokentelephone.game.features.sign_in.use_case.SignInUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<SignInSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email, credentialsError = null) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password, credentialsError = null) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onSignInClick() {
        val currentState = state.value.copy(email = state.value.email.trim())
        if (!currentState.isSignInEnabled) return

        viewModelScope.launch {
            _sideEffects.send(SignInSideEffect.ClearFocus)
            _state.update { it.copy(isLoading = true, credentialsError = null) }

            signInUseCase.execute(currentState.email, currentState.password).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _sideEffects.send(SignInSideEffect.SignedIn)
            }.onError { error ->
                val message = exceptionToMessageMapper.map(error)
                _state.update {
                    if (error is InvalidCredentialsException) {
                        it.copy(isLoading = false, credentialsError = message)
                    } else {
                        it.copy(isLoading = false, globalError = message)
                    }
                }
            }
        }
    }
}
