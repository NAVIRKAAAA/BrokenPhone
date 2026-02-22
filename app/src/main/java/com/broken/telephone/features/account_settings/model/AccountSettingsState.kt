package com.broken.telephone.features.account_settings.model

data class AccountSettingsState(
    val blockedUsersCount: Int = 0,
    val isDeleteAccountDialogVisible: Boolean = false,
    val isDeleteAccountLoading: Boolean = false,
)
