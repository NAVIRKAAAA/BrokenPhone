package com.brokentelephone.game.features.edit_bio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.api_handler.onError
import com.brokentelephone.game.domain.api_handler.onSuccess
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.edit_bio.model.EditBioEvent
import com.brokentelephone.game.features.edit_bio.model.EditBioState
import com.brokentelephone.game.features.edit_bio.use_case.UpdateBioUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditBioViewModel(
    private val updateBioUseCase: UpdateBioUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(EditBioState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditBioEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {

            val user = getCurrentUserUseCase.execute().firstOrNull() ?: return@launch
            _state.update { it.copy(bio = user.bio, initialBio = user.bio) }
        }
    }

    fun onBioChange(bio: String) {
        _state.update { it.copy(bio = bio) }
    }

    fun onSave() {
        if (!_state.value.isSaveEnabled) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            updateBioUseCase.execute(_state.value.bio).onSuccess {
                _state.update { it.copy(isLoading = false) }
                _event.emit(EditBioEvent.NavigateBack)
            }.onError { error ->
                _state.update { it.copy(isLoading = false, globalError = exceptionToMessageMapper.map(error)) }
            }
        }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }

    companion object {
        const val MAX_BIO_LENGTH = 150
    }
}
