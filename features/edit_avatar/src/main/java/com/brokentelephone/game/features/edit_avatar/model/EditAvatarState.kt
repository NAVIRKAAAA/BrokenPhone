package com.brokentelephone.game.features.edit_avatar.model

data class EditAvatarState(
    val avatarUi: AvatarUi? = null,
    val loadingAvatarId: Int? = null,
    val globalError: String? = null,
)
