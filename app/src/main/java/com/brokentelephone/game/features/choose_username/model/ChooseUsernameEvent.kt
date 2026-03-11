package com.brokentelephone.game.features.choose_username.model

sealed interface ChooseUsernameEvent {
    data class NavigateNext(val username: String) : ChooseUsernameEvent
    data object NavigateBack : ChooseUsernameEvent
}
