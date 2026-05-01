package com.brokentelephone.game.features.account_settings.model

import com.brokentelephone.game.core.model.user.UserUi

data class AccountSettingsState(
    val user: UserUi? = null,
    val isDeleteAccountDialogVisible: Boolean = false,
    val isDeleteAccountLoading: Boolean = false,
    val isVerifyEmailDialogVisible: Boolean = false,
    val isVerifyEmailLoading: Boolean = false,
    val globalError: String? = null,
)
