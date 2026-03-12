package com.brokentelephone.game.features.notifications.use_case

import com.brokentelephone.game.domain.settings.NotificationType
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotificationsUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<List<NotificationType>> {
        return userSession.authState.map { authState ->
            authState.getUserOrNull()?.notifications ?: emptyList()
        }
    }
}
