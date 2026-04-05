package com.brokentelephone.game.features.notifications_settings.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.api_handler.AppResult
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.user.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

class UpdateUserPermissionsUseCase(
    private val userSession: UserSession,
    private val handler: ApiHandler,
) {
    suspend fun execute(permissions: UserPermissions): AppResult<Unit> {
        return handler.handle(Dispatchers.IO) {
            val current = userSession.authState.first().getUserOrNull()?.permissions
            if (current == permissions) return@handle
            userSession.updatePermissions(permissions)
        }
    }
}
