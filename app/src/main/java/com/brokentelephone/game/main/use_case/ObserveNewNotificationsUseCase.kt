package com.brokentelephone.game.main.use_case

import android.util.Log
import com.brokentelephone.game.domain.banner.BannerController
import com.brokentelephone.game.domain.model.banner.BannerType
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationData
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest

class ObserveNewNotificationsUseCase(
    private val notificationsRepository: NotificationsRepository,
    private val userSession: UserSession,
    private val bannerController: BannerController
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun execute() {
        userSession.getAuthUserOrNull()
            .distinctUntilChangedBy { it?.id }
            .flatMapLatest { user ->
                if (user != null) {
                    Log.d(TAG, "User readNotificationIds: ${user.readNotificationIds}")
                    val unreadNotifications = runCatching {
                        notificationsRepository.getNotifications(user.id)
                            .filter { it.id !in user.readNotificationIds }
                    }.getOrElse { emptyList() }

                    Log.d(TAG, "unreadNotifications(): ${unreadNotifications.size}")

                    userSession.updateUnreadNotifications(unreadNotifications)
                    notificationsRepository.observeNewNotifications()
                } else {
                    emptyFlow()
                }
            }.collect { notification ->
                Log.d(TAG, "ObserveNewNotificationsUseCase(): $notification")
                userSession.addUnreadNotification(notification)
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

    private companion object {
        const val TAG = "ObserveNewNotificationsUseCase"

        const val DURATION_SECONDS = 10
        const val DURATION_MS = DURATION_SECONDS * 1000L
    }
}
