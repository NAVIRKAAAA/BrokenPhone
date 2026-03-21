package com.brokentelephone.game.features.forgot_password

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.forgot_password.model.ForgotPasswordSideEffect
import com.brokentelephone.game.features.forgot_password.model.ForgotPasswordState
import com.brokentelephone.game.features.forgot_password.use_case.SendPasswordResetEmailUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    initialEmail: String,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(
        ForgotPasswordState(
            email = TextFieldValue(
                text = initialEmail,
                selection = TextRange(initialEmail.length)
            )
        )
    )
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()

    private val _sideEffects = Channel<ForgotPasswordSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEmailChanged(email: TextFieldValue) {
        _state.update { it.copy(email = email) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onResetLinkSentDismissed() {
        _state.update { it.copy(isResetLinkSent = false) }

        viewModelScope.launch {
            _sideEffects.send(ForgotPasswordSideEffect.NavigateBack)
        }
    }

    fun onSendClick() {
        val email = state.value.email.text.trim()
        if (!state.value.isSendEnabled) return

        viewModelScope.launch {
            _sideEffects.send(ForgotPasswordSideEffect.ClearFocus)
            _state.update { it.copy(isLoading = true) }

            sendPasswordResetEmailUseCase.execute(email).onSuccess {
                _state.update { it.copy(isLoading = false, isResetLinkSent = true) }
            }.onError { error ->
                val message = exceptionToMessageMapper.map(error)
                _state.update { it.copy(isLoading = false, globalError = message) }
            }
        }
    }
}
