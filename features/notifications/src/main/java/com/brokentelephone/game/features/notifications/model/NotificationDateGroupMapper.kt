package com.brokentelephone.game.features.notifications.model

import com.brokentelephone.game.core.model.notification.NotificationUi
import java.util.Calendar

fun List<NotificationUi>.groupByDate(): Map<NotificationDateGroup, List<NotificationUi>> {
    val now = Calendar.getInstance()

    val today = now.toMidnight()
    val yesterday = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -1) }
    val last7Days = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -7) }
    val last30Days = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_YEAR, -30) }

    return groupBy { notification ->
        val notifDay = Calendar.getInstance().apply {
            timeInMillis = notification.createdAt
        }.toMidnight()

        when {
            !notifDay.before(today) -> NotificationDateGroup.TODAY
            !notifDay.before(yesterday) -> NotificationDateGroup.YESTERDAY
            !notifDay.before(last7Days) -> NotificationDateGroup.LAST_7_DAYS
            !notifDay.before(last30Days) -> NotificationDateGroup.LAST_30_DAYS
            else -> NotificationDateGroup.EARLIER
        }
    }.toSortedMap(compareBy { it.ordinal })
}

private fun Calendar.toMidnight(): Calendar = (clone() as Calendar).apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}
