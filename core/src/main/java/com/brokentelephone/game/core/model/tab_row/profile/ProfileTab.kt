package com.brokentelephone.game.core.model.tab_row.profile

import androidx.annotation.StringRes
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.model.tab_row.BTTabRow

enum class ProfileTab(@param:StringRes override val labelResId: Int) : BTTabRow {
    POSTS(R.string.profile_posts),
    CONTRIBUTIONS(R.string.profile_contributions),
}