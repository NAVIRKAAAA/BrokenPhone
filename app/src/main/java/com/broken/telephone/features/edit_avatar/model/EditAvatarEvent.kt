package com.broken.telephone.features.edit_avatar.model

sealed class EditAvatarEvent {
    data object NavigateBack : EditAvatarEvent()
}
