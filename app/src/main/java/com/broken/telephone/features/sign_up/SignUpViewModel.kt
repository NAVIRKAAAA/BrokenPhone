package com.broken.telephone.features.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.domain.auth.SignUpResult
import com.broken.telephone.features.sign_up.model.SignUpSideEffect
import com.broken.telephone.features.sign_up.model.SignUpState
import com.broken.telephone.features.sign_up.use_case.SignUpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val signUpValidator: SignUpValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<SignUpSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun onSignUpClick() {
        val currentState = state.value
        if (!currentState.isSignUpEnabled) return

        if (currentState.password != currentState.confirmPassword) {
            _state.update { it.copy(confirmPasswordError = "Passwords do not match") }
            return
        }

        val localError = signUpValidator.validate(currentState.email, currentState.password)
        if (localError != null) {
            when (localError) {
                SignUpResult.Error.InvalidEmail -> _state.update { it.copy(emailError = "Invalid email address") }
                SignUpResult.Error.PasswordTooShort -> _state.update { it.copy(passwordError = "At least 8 characters required") }
                else -> Unit
            }
            return
        }

        viewModelScope.launch {
            _sideEffects.send(SignUpSideEffect.ClearFocus)
            _state.update { it.copy(isLoading = true, emailError = null, passwordError = null, confirmPasswordError = null) }

            val result = signUpUseCase(currentState.email, currentState.password, currentState.confirmPassword)

            when (result) {
                SignUpResult.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _sideEffects.send(SignUpSideEffect.SignedUp)
                }
                SignUpResult.Error.InvalidEmail -> {
                    _state.update { it.copy(isLoading = false, emailError = "Invalid email address") }
                }
                SignUpResult.Error.PasswordTooShort -> {
                    _state.update { it.copy(isLoading = false, passwordError = "At least 8 characters required") }
                }
                SignUpResult.Error.PasswordsDoNotMatch -> {
                    _state.update { it.copy(isLoading = false, confirmPasswordError = "Passwords do not match") }
                }
            }
        }
    }
}
