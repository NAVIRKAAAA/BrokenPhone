package com.broken.telephone.features.bottom_nav_bar.model

import com.broken.telephone.R

enum class BottomNavBar(
    val title: String,
    val iconResId: Int,
) {

    DASHBOARD(
        title = "Dashboard",
        iconResId = R.drawable.ic_mutations
    ),

    CREATE(
        title = "Create",
        iconResId = R.drawable.ic_plus
    ),

    PROFILE(
        title = "Profile",
        iconResId = R.drawable.ic_profile
    )

}