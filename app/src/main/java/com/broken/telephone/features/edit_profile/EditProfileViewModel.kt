package com.broken.telephone.features.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import com.broken.telephone.features.edit_profile.model.EditProfileEvent
import com.broken.telephone.features.edit_profile.model.EditProfileState
import com.broken.telephone.features.edit_profile.use_case.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val userSession: UserSession,
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditProfileEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            val authState = userSession.authState.first()
            if (authState is AuthState.Auth) {
                _state.update { it.copy(username = authState.user.username) }
            }
        }
    }

    fun onUsernameChange(username: String) {
        _state.update { it.copy(username = username, isSaveEnabled = username.isNotBlank()) }
    }

    fun onSave() {
        viewModelScope.launch {
            updateProfileUseCase(_state.value.username)
            _event.emit(EditProfileEvent.NavigateBack)
        }
    }
}
