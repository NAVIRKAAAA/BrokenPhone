package com.brokentelephone.game.main.use_case

import android.util.Log
import com.brokentelephone.game.data.notifications.NotificationObserver
import com.brokentelephone.game.domain.banner.BannerController
import com.brokentelephone.game.domain.model.banner.BannerType
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationData

class ObserveNewNotificationsUseCase(
    private val observer: NotificationObserver,
    private val bannerController: BannerController
) {
    suspend fun execute() {
        observer.observe().collect { notification ->
            Log.d("LOG_TAG", "ObserveNewNotificationsUseCase(): $notification")
            val banner = notification.toBannerOrNull() ?: return@collect
            bannerController.show(banner)
        }
    }

    private fun Notification.toBannerOrNull(): BannerType? = when (val data = data) {
        is NotificationData.News -> BannerType.NewsNotification(
            id = id,
            title = data.title,
            body = data.body,
            expiresAt = System.currentTimeMillis() + DURATION_MS,
            remainingSeconds = DURATION_SECONDS,
            totalSeconds = DURATION_SECONDS,
        )

        is NotificationData.Friends,
        is NotificationData.ChainInfo -> null
    }

    companion object {
        private const val DURATION_SECONDS = 10
        private const val DURATION_MS = DURATION_SECONDS * 1000L
    }
}
