package com.brokentelephone.game.features.edit_avatar.model

sealed class EditAvatarEvent {
    data object ScrollToTop : EditAvatarEvent()
}
