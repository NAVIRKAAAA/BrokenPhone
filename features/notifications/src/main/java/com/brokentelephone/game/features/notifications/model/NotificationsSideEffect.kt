package com.brokentelephone.game.features.notifications.model

sealed interface NotificationsSideEffect {
    data object ScrollToTop : NotificationsSideEffect
    data class NavigateToUserDetails(val userId: String) : NotificationsSideEffect
    data class NavigateToChainDetails(val postId: String, val userId: String) : NotificationsSideEffect
}
