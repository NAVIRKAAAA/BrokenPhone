package com.brokentelephone.game.domain.model.notification

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class NotificationFilter(
    @param:StringRes val labelResId: Int,
    @param:StringRes val emptyTitleResId: Int,
    @param:StringRes val emptyBodyResId: Int,
) {
    ALL(
        labelResId = R.string.notification_filter_all,
        emptyTitleResId = R.string.notifications_empty_title_all,
        emptyBodyResId = R.string.notifications_empty_body_all,
    ),
    UNREAD(
        labelResId = R.string.notification_filter_unread,
        emptyTitleResId = R.string.notifications_empty_title_unread,
        emptyBodyResId = R.string.notifications_empty_body_unread,
    ),
    NEWS(
        labelResId = R.string.notification_filter_news,
        emptyTitleResId = R.string.notifications_empty_title_news,
        emptyBodyResId = R.string.notifications_empty_body_news,
    ),
    FRIENDS(
        labelResId = R.string.notification_filter_friends,
        emptyTitleResId = R.string.notifications_empty_title_friends,
        emptyBodyResId = R.string.notifications_empty_body_friends,
    ),
    CHAIN(
        labelResId = R.string.notification_filter_chain,
        emptyTitleResId = R.string.notifications_empty_title_chain,
        emptyBodyResId = R.string.notifications_empty_body_chain,
    ),
}
