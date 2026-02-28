package com.broken.telephone.core.utils

import android.text.format.DateUtils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberRelativeTime(timestamp: Long): String = remember(timestamp) {
    DateUtils.getRelativeTimeSpanString(
        timestamp,
        System.currentTimeMillis(),
        DateUtils.MINUTE_IN_MILLIS,
        DateUtils.FORMAT_ABBREV_ALL
    ).toString()
}
