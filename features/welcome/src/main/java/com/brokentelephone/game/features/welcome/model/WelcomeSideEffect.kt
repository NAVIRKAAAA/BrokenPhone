package com.brokentelephone.game.features.welcome.model

sealed class WelcomeSideEffect {
    data object NavigateToDashboard : WelcomeSideEffect()
}
