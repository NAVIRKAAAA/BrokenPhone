package com.brokentelephone.game.features.account_settings.model

sealed interface AccountSettingsSideEffect {
    data object NavigateToWelcome : AccountSettingsSideEffect
}
