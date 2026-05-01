package com.brokentelephone.game.features.new_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.essentials.exceptions.auth.PasswordsDoNotMatchException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.new_password.model.NewPasswordSideEffect
import com.brokentelephone.game.features.new_password.model.NewPasswordState
import com.brokentelephone.game.features.new_password.use_case.UpdatePasswordUseCase
import com.brokentelephone.game.features.new_password.use_case.ValidateNewPasswordUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPasswordViewModel(
    private val updatePasswordUseCase: UpdatePasswordUseCase,
    private val getPendingEmailUseCase: GetPendingEmailUseCase,
    private val validateNewPasswordUseCase: ValidateNewPasswordUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(NewPasswordState())
    val state: StateFlow<NewPasswordState> = _state.asStateFlow()

    private val _sideEffects = Channel<NewPasswordSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onPasswordChanged(value: String) {
        _state.update { it.copy(password = value, passwordError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _state.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onToggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    fun onSaveClick() {
        if (!_state.value.isSaveEnabled) return

        viewModelScope.launch {
            val state = _state.value
            val errors = validateNewPasswordUseCase(state.password, state.confirmPassword)
            if (errors.isNotEmpty()) {
                _state.update {
                    errors.fold(it) { acc, error ->
                        val message = exceptionToMessageMapper.map(error)
                        when (error) {
                            is WeakPasswordException -> acc.copy(passwordError = message)
                            is PasswordsDoNotMatchException -> acc.copy(confirmPasswordError = message)
                            else -> acc
                        }
                    }
                }
                return@launch
            }

            _state.update { it.copy(isLoading = true) }
            updatePasswordUseCase.execute(_state.value.password)
                .onSuccess {
                    val email = getPendingEmailUseCase.execute().orEmpty()
                    _state.update { it.copy(isLoading = false) }
                    _sideEffects.send(NewPasswordSideEffect.NavigateToSignIn(email))
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            confirmPasswordError = exceptionToMessageMapper.map(error),
                        )
                    }
                }
        }
    }
}
