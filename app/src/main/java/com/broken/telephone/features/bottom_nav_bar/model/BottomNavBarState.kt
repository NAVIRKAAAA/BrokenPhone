package com.broken.telephone.features.bottom_nav_bar.model

data class BottomNavBarState(
    val selectedItem: BottomNavBar = BottomNavBar.DASHBOARD,
    val isVisibleByScroll: Boolean = true,
    val userAvatarUrl: String? = null,
    val isAuth: Boolean = false,
)