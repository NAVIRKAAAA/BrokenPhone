package com.brokentelephone.game.features.sign_in

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.SignInWithGoogleUseCase
import com.brokentelephone.game.essentials.exceptions.auth.GoogleSignInCancelledException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidCredentialsException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.settings.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.features.settings.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.features.sign_in.model.SignInSideEffect
import com.brokentelephone.game.features.sign_in.model.SignInState
import com.brokentelephone.game.features.sign_in.use_case.SignInWithEmailPasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val signInWithEmailPasswordUseCase: SignInWithEmailPasswordUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
    private val getTermsOfServiceLinkUseCase: GetTermsOfServiceLinkUseCase,
    private val getPrivacyPolicyLinkUseCase: GetPrivacyPolicyLinkUseCase,
    initialEmail: String = "",
) : ViewModel() {

    private val _state = MutableStateFlow(
        SignInState(email = TextFieldValue(initialEmail, TextRange(initialEmail.length)))
    )
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<SignInSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEmailChanged(email: TextFieldValue) {
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

    fun onTermsClick() {
        viewModelScope.launch {
            _sideEffects.send(SignInSideEffect.OpenLink(getTermsOfServiceLinkUseCase()))
        }
    }

    fun onPrivacyPolicyClick() {
        viewModelScope.launch {
            _sideEffects.send(SignInSideEffect.OpenLink(getPrivacyPolicyLinkUseCase()))
        }
    }

    fun onGoogleSignInClick() {
        if (_state.value.isGoogleLoading || _state.value.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isGoogleLoading = true) }

            signInWithGoogleUseCase.execute().onSuccess { isNewUser ->
                _state.update { it.copy(isGoogleLoading = false) }
                if (isNewUser) {
                    _sideEffects.send(SignInSideEffect.NavigateToChooseAvatar)
                } else {
                    _sideEffects.send(SignInSideEffect.SignedIn)
                }
            }.onError { exception ->
                if(exception !is GoogleSignInCancelledException) {

                    delay(150)

                    _state.update { it.copy(globalError = exceptionToMessageMapper.map(exception)) }
                }

                _state.update { it.copy(isGoogleLoading = false) }

            }
        }
    }

    fun onSignInClick() {
        val trimmedEmail = state.value.email.text.trim()
        _state.update { it.copy(email = it.email.copy(text = trimmedEmail)) }
        val currentState = _state.value
        if (!currentState.isSignInEnabled) return

        viewModelScope.launch {
            _sideEffects.send(SignInSideEffect.ClearFocus)
            _state.update { it.copy(isLoading = true, credentialsError = null) }

            signInWithEmailPasswordUseCase.execute(currentState.email.text, currentState.password).onSuccess {
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
