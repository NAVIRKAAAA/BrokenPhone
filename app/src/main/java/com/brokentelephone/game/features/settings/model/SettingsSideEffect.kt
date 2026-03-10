package com.brokentelephone.game.features.settings.model

sealed interface SettingsSideEffect {
    data object NavigateToWelcome : SettingsSideEffect
    data class OpenLink(val url: String) : SettingsSideEffect
}
