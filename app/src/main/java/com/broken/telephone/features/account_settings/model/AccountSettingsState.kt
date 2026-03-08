package com.broken.telephone.features.account_settings.model

import com.broken.telephone.features.profile.model.UserUi

data class AccountSettingsState(
    val user: UserUi? = null,
    val blockedUsersCount: Int = 0,
    val isDeleteAccountDialogVisible: Boolean = false,
    val isDeleteAccountLoading: Boolean = false,
)
