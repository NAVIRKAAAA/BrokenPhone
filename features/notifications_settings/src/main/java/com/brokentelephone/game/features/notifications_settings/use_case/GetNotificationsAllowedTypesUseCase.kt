package com.brokentelephone.game.features.notifications_settings.use_case

import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotificationsAllowedTypesUseCase(
    private val userSession: UserSession,
) {
    fun execute(): Flow<List<NotificationType>> {
        return userSession.getAuthUserOrNull().map { user ->
            user?.notifications ?: NotificationType.entries
        }
    }
}
