package com.brokentelephone.game.features.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.SignInWithGoogleUseCase
import com.brokentelephone.game.essentials.exceptions.auth.GoogleSignInCancelledException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.auth.PasswordsDoNotMatchException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.settings.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.features.settings.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.features.sign_up.model.SignUpSideEffect
import com.brokentelephone.game.features.sign_up.model.SignUpState
import com.brokentelephone.game.features.sign_up.use_case.SignUpUseCase
import com.brokentelephone.game.features.sign_up.use_case.ValidateSignUpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val signUpUseCase: SignUpUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val validateSignUpUseCase: ValidateSignUpUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val getTermsOfServiceLinkUseCase: GetTermsOfServiceLinkUseCase,
    private val getPrivacyPolicyLinkUseCase: GetPrivacyPolicyLinkUseCase,
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

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onTermsClick() {
        viewModelScope.launch {
            _sideEffects.send(SignUpSideEffect.OpenLink(getTermsOfServiceLinkUseCase()))
        }
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _sideEffects.send(SignUpSideEffect.OpenLink(getPrivacyPolicyLinkUseCase()))
        }
    }

    fun onGoogleSignInClick() {
        if (_state.value.isGoogleLoading || _state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isGoogleLoading = true) }

            signInWithGoogleUseCase.execute().onSuccess { isNewUser ->
                _state.update { it.copy(isGoogleLoading = false) }
                if (isNewUser) {
                    _sideEffects.send(SignUpSideEffect.NavigateToChooseAvatar)
                } else {
                    _sideEffects.send(SignUpSideEffect.SignedUp)
                }
            }.onError { exception ->
                if (exception !is GoogleSignInCancelledException) {
                    _state.update { it.copy(isGoogleLoading = false, globalError = exceptionToMessageMapper.map(exception)) }
                } else {
                    _state.update { it.copy(isGoogleLoading = false) }
                }
            }
        }
    }

    fun onSignUpClick() {
        val currentState = state.value.copy(email = state.value.email.trim())
        if (!currentState.isSignUpEnabled) return

        val validationErrors = validateSignUpUseCase(
            email = currentState.email,
            password = currentState.password,
            confirmPassword = currentState.confirmPassword,
        )
        if (validationErrors.isNotEmpty()) {
            _state.update { state ->
                validationErrors.fold(state) { acc, error ->
                    val message = exceptionToMessageMapper.map(error)
                    when (error) {
                        is InvalidEmailException -> acc.copy(emailError = message)
                        is WeakPasswordException -> acc.copy(passwordError = message)
                        is PasswordsDoNotMatchException -> acc.copy(confirmPasswordError = message)
                        else -> acc
                    }
                }
            }
            return
        }

        viewModelScope.launch {
            _sideEffects.send(SignUpSideEffect.ClearFocus)
            _state.update { it.copy(isLoading = true, emailError = null, passwordError = null, confirmPasswordError = null) }

            signUpUseCase.execute(currentState.email, currentState.password).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _sideEffects.send(SignUpSideEffect.NavigateToChooseAvatar)
            }.onError { error ->
                val message = exceptionToMessageMapper.map(error)
                _state.update { it.copy(isLoading = false, globalError = message) }
            }
        }
    }
}