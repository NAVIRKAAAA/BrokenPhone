package com.brokentelephone.game.features.notifications_settings.use_case

import android.content.Context
import androidx.core.app.NotificationManagerCompat

class CheckNotificationPermissionUseCase(private val context: Context) {
    operator fun invoke(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}
