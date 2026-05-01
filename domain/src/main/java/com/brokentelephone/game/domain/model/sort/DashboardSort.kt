package com.brokentelephone.game.domain.model.sort

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class DashboardSort(@param:StringRes val labelResId: Int) {
    LATEST(R.string.dashboard_sort_latest),
    ALMOST_DONE(R.string.dashboard_sort_almost_done),
    LONGEST_CHAIN(R.string.dashboard_sort_longest_chain),
}