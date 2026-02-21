package com.broken.telephone.features.edit_avatar.model

import com.broken.telephone.features.profile.model.UserUi

data class EditAvatarState(
    val user: UserUi? = null,
    val initialAvatarId: Int? = null,
    val selectedAvatarId: Int? = null,
) {
    val isSaveEnabled: Boolean get() = selectedAvatarId != null && selectedAvatarId != initialAvatarId
}
