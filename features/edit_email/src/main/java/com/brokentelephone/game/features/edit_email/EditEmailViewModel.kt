package com.brokentelephone.game.features.edit_email

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.edit_email.model.EditEmailEvent
import com.brokentelephone.game.features.edit_email.model.EditEmailState
import com.brokentelephone.game.features.edit_email.use_case.SendEmailChangeVerificationUseCase
import com.brokentelephone.game.features.edit_email.use_case.ValidateEmailUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditEmailViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val sendEmailChangeVerificationUseCase: SendEmailChangeVerificationUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(EditEmailState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditEmailEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            val user = getCurrentUserUseCase().first() ?: return@launch
            _state.update {
                it.copy(
                    email = TextFieldValue(user.email, TextRange(user.email.length)),
                    initialEmail = user.email,
                )
            }
        }
    }

    fun onEmailChange(email: TextFieldValue) {
        _state.update { it.copy(email = email, emailError = null) }
    }

    fun onSave() {
        val trimmedEmail = state.value.email.text.trim()
        _state.update { it.copy(email = it.email.copy(text = trimmedEmail)) }
        val currentState = _state.value
        if (!currentState.isSaveEnabled) return

        val validationErrors = validateEmailUseCase.execute(
            email = currentState.email.text
        )
        if (validationErrors.isNotEmpty()) {
            _state.update { state ->
                validationErrors.fold(state) { acc, error ->
                    val message = exceptionToMessageMapper.map(error)
                    when (error) {
                        is InvalidEmailException -> acc.copy(emailError = message)
                        else -> acc
                    }
                }
            }
            return
        }

        _state.update { it.copy(showConfirmDialog = true) }
    }

    fun onSaveConfirmed() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            sendEmailChangeVerificationUseCase.execute(_state.value.email.text)
                .onSuccess {
                    _state.update { it.copy(isLoading = false, showConfirmDialog = false) }
                    _event.emit(EditEmailEvent.NavigateBack)
                }
                .onError { error ->
                    val message = exceptionToMessageMapper.map(error)
                    _state.update { it.copy(isLoading = false, showConfirmDialog = false, globalError = message) }
                }
        }
    }

    fun onSaveDialogDismissed() {
        _state.update { it.copy(showConfirmDialog = false) }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }
}
