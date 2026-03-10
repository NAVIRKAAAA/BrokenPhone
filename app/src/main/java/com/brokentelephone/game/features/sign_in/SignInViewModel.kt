package com.brokentelephone.game.features.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.auth.SignInResult
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

    fun onSignInClick() {
        val currentState = state.value
        if (!currentState.isSignInEnabled) return

        viewModelScope.launch {
            _sideEffects.send(SignInSideEffect.ClearFocus)
            _state.update { it.copy(isLoading = true, credentialsError = null) }

            when (signInUseCase(currentState.email, currentState.password)) {
                SignInResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffects.send(SignInSideEffect.SignedIn)
                }
                SignInResult.Error.InvalidCredentials -> {
                    _state.update { it.copy(isLoading = false, credentialsError = "Invalid email or password") }
                }
            }
        }
    }
}
