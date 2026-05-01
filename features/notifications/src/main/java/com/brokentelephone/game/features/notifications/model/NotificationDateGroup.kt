package com.brokentelephone.game.features.notifications.model

import com.brokentelephone.game.core.R

enum class NotificationDateGroup(val labelRes: Int) {
    TODAY(R.string.notification_group_today),
    YESTERDAY(R.string.notification_group_yesterday),
    LAST_7_DAYS(R.string.notification_group_last_7_days),
    LAST_30_DAYS(R.string.notification_group_last_30_days),
    EARLIER(R.string.notification_group_earlier),
}
