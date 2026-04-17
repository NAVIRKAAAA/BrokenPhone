package com.brokentelephone.game.main

import com.brokentelephone.game.describe_drawing_api.DescribeDrawingRoute
import com.brokentelephone.game.draw_api.DrawRoute

sealed interface MainSideEffect {
    data class NavigateToDraw(val route: DrawRoute) : MainSideEffect
    data class NavigateToDescribeDrawing(val route: DescribeDrawingRoute) : MainSideEffect
    data class NavigateToSignIn(val email: String) : MainSideEffect
    data object NavigateToNewPassword : MainSideEffect
}
