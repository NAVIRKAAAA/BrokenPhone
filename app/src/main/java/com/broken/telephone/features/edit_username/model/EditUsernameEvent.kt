package com.broken.telephone.features.edit_username.model

sealed class EditUsernameEvent {
    data object NavigateBack : EditUsernameEvent()
}
