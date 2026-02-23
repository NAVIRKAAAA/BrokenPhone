package com.broken.telephone.features.notifications.use_case

import com.broken.telephone.domain.settings.NotificationType
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotificationsUseCase(
    private val userSession: UserSession,
) {
    operator fun invoke(): Flow<List<NotificationType>> {
        return userSession.authState.map { authState ->
            when (authState) {
                is AuthState.Auth -> authState.user.enabledNotifications
                else -> emptyList()
            }
        }
    }
}
