package com.brokentelephone.game.features.confirm_sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.confirm_sign_up.model.ConfirmSignUpSideEffect
import com.brokentelephone.game.features.confirm_sign_up.model.ConfirmSignUpState
import com.brokentelephone.game.features.confirm_sign_up.model.OTP_CODE_LENGTH
import com.brokentelephone.game.features.confirm_sign_up.use_case.ResendSignUpConfirmationUseCase
import com.brokentelephone.game.features.confirm_sign_up.use_case.VerifyEmailOtpUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val RESEND_COOLDOWN_SECONDS = 60

class ConfirmSignUpViewModel(
    email: String,
    private val countdownTimer: CountdownTimer,
    private val verifyEmailOtpUseCase: VerifyEmailOtpUseCase,
    private val resendSignUpConfirmationUseCase: ResendSignUpConfirmationUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(ConfirmSignUpState(email = email))
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<ConfirmSignUpSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        startResendCooldown()
    }

    fun onConfirmClick() {
        viewModelScope.launch {
            _state.update { it.copy(isConfirmLoading = true) }
            verifyEmailOtpUseCase.execute(_state.value.email, _state.value.code)
                .onSuccess {
                    _state.update { it.copy(isConfirmLoading = false) }
                    _sideEffects.send(ConfirmSignUpSideEffect.EmailVerified)
                }
                .onError { error ->
                    _state.update { it.copy(isConfirmLoading = false, globalError = exceptionToMessageMapper.map(error)) }
                }
        }
    }

    fun onCodeChange(code: String) {
        val filtered = code.filter { it.isDigit() }.take(OTP_CODE_LENGTH)
        _state.update { it.copy(code = filtered) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    fun onResendClick() {
        if (!_state.value.isResendEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isResendLoading = true) }
            resendSignUpConfirmationUseCase.execute(_state.value.email)
                .onSuccess {
                    _state.update { it.copy(isResendLoading = false) }
                    startResendCooldown()
                }
                .onError { error ->
                    _state.update { it.copy(isResendLoading = false, globalError = exceptionToMessageMapper.map(error)) }
                }
        }
    }

    private fun startResendCooldown() {
        countdownTimer.start(RESEND_COOLDOWN_SECONDS)
            .onEach { remaining -> _state.update { it.copy(resendCooldownSeconds = remaining) } }
            .launchIn(viewModelScope)
    }
}
