package com.brokentelephone.game.features.edit_avatar.model

import com.brokentelephone.game.features.profile.model.UserUi

data class EditAvatarState(
    val user: UserUi? = null,
    val initialAvatarId: Int? = null,
    val selectedAvatarId: Int? = null,
) {
    val isSaveEnabled: Boolean get() = selectedAvatarId != null && selectedAvatarId != initialAvatarId
}
