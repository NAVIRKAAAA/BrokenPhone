package com.broken.telephone.features.dashboard.model

import androidx.annotation.StringRes
import com.broken.telephone.R

enum class DashboardSort(@param:StringRes val labelResId: Int) {
    JUST_STARTED(R.string.dashboard_sort_just_started),
    ALMOST_DONE(R.string.dashboard_sort_almost_done),
    LONGEST_CHAIN(R.string.dashboard_sort_longest_chain),
}
