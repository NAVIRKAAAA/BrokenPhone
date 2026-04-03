package com.brokentelephone.game.domain.model.notification

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class NotificationFilter(@param:StringRes val labelResId: Int) {
    ALL(R.string.notification_filter_all),
    UNREAD(R.string.notification_filter_unread),
    NEWS(R.string.notification_filter_news),
    FRIENDS(R.string.notification_filter_friends),
    CHAIN(R.string.notification_filter_chain),
}
