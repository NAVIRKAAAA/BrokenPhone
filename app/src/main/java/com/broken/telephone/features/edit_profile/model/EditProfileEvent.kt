package com.broken.telephone.features.edit_profile.model

sealed class EditProfileEvent {
    data object NavigateBack : EditProfileEvent()
}
