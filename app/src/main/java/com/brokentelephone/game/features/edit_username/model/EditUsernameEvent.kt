package com.brokentelephone.game.features.edit_username.model

sealed class EditUsernameEvent {
    data object NavigateBack : EditUsernameEvent()
}
