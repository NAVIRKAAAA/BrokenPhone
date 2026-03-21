package com.brokentelephone.game.features.edit_email.model

sealed class EditEmailEvent {
    data object NavigateBack : EditEmailEvent()
    data object NavigateToSignIn : EditEmailEvent()
}
