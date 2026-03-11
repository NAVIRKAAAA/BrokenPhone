package com.brokentelephone.game.features.choose_avatar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brokentelephone.game.features.choose_avatar.model.ChooseAvatarEvent
import com.brokentelephone.game.features.edit_avatar.model.AvatarUi
import com.brokentelephone.game.features.edit_avatar.model.Avatars
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ChooseAvatarViewModel : ViewModel() {

    private val _event = MutableSharedFlow<ChooseAvatarEvent>()
    val event = _event.asSharedFlow()

    fun onAvatarClick(avatar: AvatarUi) {
        val avatarUrl = Avatars.all.first { it.id == avatar.id }.url
        viewModelScope.launch {
            _event.emit(ChooseAvatarEvent.AvatarSelected(avatarUrl))
        }
    }
}
