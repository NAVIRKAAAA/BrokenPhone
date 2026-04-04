package com.brokentelephone.game.data.fcm

import com.brokentelephone.game.domain.use_case.UpdateFcmTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmTokenService : FirebaseMessagingService() {

    private val updateFcmTokenUseCase: UpdateFcmTokenUseCase by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        scope.launch {
            updateFcmTokenUseCase.execute(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // TODO: handle incoming push notifications
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
