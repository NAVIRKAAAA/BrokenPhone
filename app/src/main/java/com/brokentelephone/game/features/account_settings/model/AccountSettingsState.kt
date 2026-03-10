package com.brokentelephone.game.features.account_settings.model

import com.brokentelephone.game.features.profile.model.UserUi

data class AccountSettingsState(
    val user: UserUi? = null,
    val blockedUsersCount: Int = 0,
    val isDeleteAccountDialogVisible: Boolean = false,
    val isDeleteAccountLoading: Boolean = false,
)
