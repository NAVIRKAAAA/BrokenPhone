package com.brokentelephone.game.domain.model.settings

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class NotificationType(@param:StringRes val displayNameResId: Int) {
    NEWS(R.string.notifications_type_news),
//    NEW_POSTS(R.string.notifications_type_new_posts),
    CHAIN_COMPLETED(R.string.notifications_type_chain_completed),
}
