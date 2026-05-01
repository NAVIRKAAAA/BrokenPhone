package com.brokentelephone.game.data.fcm

import android.util.Log
import com.brokentelephone.game.domain.model.banner.BannerType
import com.brokentelephone.game.domain.use_case.ShowBannerUseCase
import com.brokentelephone.game.domain.use_case.UpdateFcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class BTFCMService : FirebaseMessagingService() {

    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase by inject()
    private val showBannerUseCase: ShowBannerUseCase by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        scope.launch {
            updateFcmTokenUseCase.execute(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        Log.d("LOG_TAG", "onMessageReceived() $data")
        when (data["type"]) {
            TYPE_NEWS -> {
                val title = message.notification?.title ?: return
                val body = message.notification?.body ?: return
                val notificationId = data["notificationId"] ?: return

                val displayDurationMs = DISPLAY_DURATION_SECONDS * 1000L
                showBannerUseCase.execute(
                    BannerType.NewsNotification(
                        id = notificationId,
                        title = title,
                        body = body,
                        expiresAt = System.currentTimeMillis() + displayDurationMs,
                        remainingSeconds = DISPLAY_DURATION_SECONDS,
                        totalSeconds = DISPLAY_DURATION_SECONDS,
                    )
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val TYPE_NEWS = "NEWS"
        private const val DISPLAY_DURATION_SECONDS = 10
    }
}
