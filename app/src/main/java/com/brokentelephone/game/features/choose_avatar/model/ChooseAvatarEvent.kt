package com.brokentelephone.game.features.choose_avatar.model

sealed interface ChooseAvatarEvent {
    data class AvatarSelected(val avatarUrl: String) : ChooseAvatarEvent
}
