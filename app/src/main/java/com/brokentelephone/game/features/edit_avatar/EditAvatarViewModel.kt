package com.brokentelephone.game.features.edit_avatar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.domain.handler.onError
import com.brokentelephone.game.domain.handler.onSuccess
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.features.edit_avatar.model.AvatarUi
import com.brokentelephone.game.features.edit_avatar.model.Avatars
import com.brokentelephone.game.features.edit_avatar.model.EditAvatarEvent
import com.brokentelephone.game.features.edit_avatar.model.EditAvatarState
import com.brokentelephone.game.features.edit_avatar.use_case.UpdateAvatarUseCase
import com.brokentelephone.game.features.profile.use_case.GetCurrentUserUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAvatarViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val exceptionToMessageMapper: ExceptionToMessageMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(EditAvatarState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditAvatarEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            val user = getCurrentUserUseCase().first()
            val avatarId = Avatars.all.find { it.url == user?.avatarUrl }?.id
            _state.update { it.copy(user = user, initialAvatarId = avatarId) }
        }
    }

    fun onAvatarClick(avatar: AvatarUi) {
        if (_state.value.loadingAvatarId != null) return
        viewModelScope.launch {
            _state.update { it.copy(loadingAvatarId = avatar.id) }
            val avatarUrl = Avatars.all.first { it.id == avatar.id }.url
            updateAvatarUseCase.execute(avatarUrl).onSuccess {
                _state.update { it.copy(loadingAvatarId = null) }
                _event.emit(EditAvatarEvent.NavigateBack)
            }.onError { error ->
                _state.update { it.copy(loadingAvatarId = null, globalError = exceptionToMessageMapper.map(error)) }
            }
        }
    }

    fun onGlobalErrorDismissed() {
        _state.update { it.copy(globalError = null) }
    }
}
