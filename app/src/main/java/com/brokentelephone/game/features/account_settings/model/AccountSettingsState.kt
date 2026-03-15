package com.brokentelephone.game.features.account_settings.model

import com.brokentelephone.game.features.profile.model.UserUi

data class AccountSettingsState(
    val user: UserUi? = null,
    val isDeleteAccountDialogVisible: Boolean = false,
    val isDeleteAccountLoading: Boolean = false,
)
