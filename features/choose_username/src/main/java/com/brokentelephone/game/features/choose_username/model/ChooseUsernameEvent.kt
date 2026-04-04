package com.brokentelephone.game.features.choose_username.model

sealed interface ChooseUsernameEvent {
    data object NavigateNext : ChooseUsernameEvent
    data object NavigateBack : ChooseUsernameEvent
}
