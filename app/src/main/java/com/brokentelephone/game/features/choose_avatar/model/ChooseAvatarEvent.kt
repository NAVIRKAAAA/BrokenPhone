package com.brokentelephone.game.features.choose_avatar.model

sealed interface ChooseAvatarEvent {
    data object NavigateToChooseUsername : ChooseAvatarEvent
}
