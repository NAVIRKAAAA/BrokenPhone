package com.broken.telephone.domain.settings

import androidx.annotation.StringRes
import com.broken.telephone.R

enum class NotificationType(@param:StringRes val displayNameResId: Int) {
    NEWS(R.string.notifications_type_news),
    NEW_POSTS(R.string.notifications_type_new_posts),
    CHAIN_COMPLETED(R.string.notifications_type_chain_completed),
}
