package com.brokentelephone.game.features.dashboard.model

import androidx.annotation.StringRes
import com.brokentelephone.game.R

enum class DashboardSort(@param:StringRes val labelResId: Int) {
    LATEST(R.string.dashboard_sort_latest),
    ALMOST_DONE(R.string.dashboard_sort_almost_done),
    LONGEST_CHAIN(R.string.dashboard_sort_longest_chain),
}
