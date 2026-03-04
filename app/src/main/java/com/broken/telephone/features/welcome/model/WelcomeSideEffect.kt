package com.broken.telephone.features.welcome.model

sealed class WelcomeSideEffect {
    data object NavigateToDashboard : WelcomeSideEffect()
}
