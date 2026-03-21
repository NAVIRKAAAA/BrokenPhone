package com.brokentelephone.game.main

import com.brokentelephone.game.navigation.routes.Routes

sealed interface MainSideEffect {
    data class NavigateToDraw(val route: Routes.Draw) : MainSideEffect
    data class NavigateToDescribeDrawing(val route: Routes.DescribeDrawing) : MainSideEffect
    data object NavigateToSignIn : MainSideEffect
}
