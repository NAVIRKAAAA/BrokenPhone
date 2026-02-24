package com.broken.telephone.features.profile.model

import androidx.annotation.StringRes
import com.broken.telephone.R

enum class ProfileTab(@param:StringRes val labelResId: Int) {
    POSTS(R.string.profile_posts),
    CONTRIBUTIONS(R.string.profile_contributions),
}
