package com.brokentelephone.game.features.bottom_nav_bar.model

import androidx.annotation.StringRes
import com.brokentelephone.game.R

enum class BottomNavBar(
    @param:StringRes val titleResId: Int,
    val iconResId: Int,
) {

    DASHBOARD(
        titleResId = R.string.bottom_nav_bar_dashboard,
        iconResId = R.drawable.ic_mutations
    ),

    CREATE(
        titleResId = R.string.bottom_nav_bar_create,
        iconResId = R.drawable.ic_plus
    ),

    PROFILE(
        titleResId = R.string.bottom_nav_bar_profile,
        iconResId = R.drawable.ic_profile
    )

}
