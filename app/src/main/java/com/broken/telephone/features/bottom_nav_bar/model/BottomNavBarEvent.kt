package com.broken.telephone.features.bottom_nav_bar.model

sealed class BottomNavBarEvent {

    data object NavigateToDashboard : BottomNavBarEvent()

    data object NavigateToCreate : BottomNavBarEvent()

    data object NavigateToProfile : BottomNavBarEvent()

}