package com.broken.telephone.features.settings.model

sealed interface SettingsSideEffect {
    data object NavigateToWelcome : SettingsSideEffect
}
