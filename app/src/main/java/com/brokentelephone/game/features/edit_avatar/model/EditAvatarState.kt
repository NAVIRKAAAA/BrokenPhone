package com.brokentelephone.game.features.edit_avatar.model

import com.brokentelephone.game.core.model.user.UserUi

data class EditAvatarState(
    val user: UserUi? = null,
    val initialAvatarId: Int? = null,
    val loadingAvatarId: Int? = null,
    val globalError: String? = null,
)
