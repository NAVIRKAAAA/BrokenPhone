package com.broken.telephone.features.account_settings.model

sealed interface AccountSettingsSideEffect {
    data object NavigateToWelcome : AccountSettingsSideEffect
}
