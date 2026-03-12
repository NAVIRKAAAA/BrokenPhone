package com.brokentelephone.game.features.edit_avatar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAvatarViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(EditAvatarState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditAvatarEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase().collect { user ->
                val avatarId = Avatars.all.find { it.url == user?.avatarUrl }?.id
                _state.update { it.copy(user = user, initialAvatarId = avatarId, selectedAvatarId = avatarId) }
            }
        }
    }

    fun onAvatarSelect(avatar: AvatarUi) {
        _state.update { it.copy(selectedAvatarId = avatar.id) }
    }

    fun onSave() {
        val selectedId = _state.value.selectedAvatarId ?: return
        val avatarUrl = Avatars.all.first { it.id == selectedId }.url
        viewModelScope.launch {
//            updateAvatarUseCase.execute(avatarUrl)
            _event.emit(EditAvatarEvent.NavigateBack)
        }
    }
}
