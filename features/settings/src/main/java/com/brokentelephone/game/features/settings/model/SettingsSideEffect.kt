package com.brokentelephone.game.features.settings.model

import com.brokentelephone.game.describe_drawing_api.DescribeDrawingRoute
import com.brokentelephone.game.draw_api.DrawRoute

sealed interface SettingsSideEffect {
    data object NavigateToWelcome : SettingsSideEffect
    data class OpenLink(val url: String) : SettingsSideEffect
    data class NavigateToDraw(val route: DrawRoute) : SettingsSideEffect
    data class NavigateToDescribeDrawing(val route: DescribeDrawingRoute) : SettingsSideEffect
}
