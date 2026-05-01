package com.brokentelephone.game.features.bottom_nav_bar.model

sealed class BottomNavBarEvent {

    data object NavigateToDashboard : BottomNavBarEvent()

    data object NavigateToCreate : BottomNavBarEvent()

    data object NavigateToProfile : BottomNavBarEvent()

    data object ScrollToTopDashboard : BottomNavBarEvent()

    data object ScrollToTopProfile : BottomNavBarEvent()

}