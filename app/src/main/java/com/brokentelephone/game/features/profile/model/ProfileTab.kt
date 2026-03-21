package com.brokentelephone.game.features.profile.model

import androidx.annotation.StringRes
import com.brokentelephone.game.core.R

enum class ProfileTab(@param:StringRes val labelResId: Int) {
    POSTS(R.string.profile_posts),
    CONTRIBUTIONS(R.string.profile_contributions),
}
