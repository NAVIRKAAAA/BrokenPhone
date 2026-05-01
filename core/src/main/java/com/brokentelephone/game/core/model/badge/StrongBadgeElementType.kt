package com.brokentelephone.game.core.model.badge

import com.brokentelephone.game.core.R

enum class StrongBadgeElementType(
    val iconResId: Int,
    val textResId: Int,
) {
    COMPLETE(
        iconResId = R.drawable.ic_complete,
        textResId = R.string.dashboard_badge_complete,
    ),
    YOU(
        iconResId = R.drawable.ic_fire,
        textResId = R.string.dashboard_badge_you,
    )
}