package com.brokentelephone.game.features.settings.model

import com.brokentelephone.game.navigation.routes.Routes

sealed interface SettingsSideEffect {
    data object NavigateToWelcome : SettingsSideEffect
    data class OpenLink(val url: String) : SettingsSideEffect
    data class NavigateToDraw(val route: Routes.Draw) : SettingsSideEffect
    data class NavigateToDescribeDrawing(val route: Routes.DescribeDrawing) : SettingsSideEffect
}
