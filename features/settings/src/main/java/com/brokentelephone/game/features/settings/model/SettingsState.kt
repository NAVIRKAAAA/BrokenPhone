package com.brokentelephone.game.features.settings.model

import com.brokentelephone.game.core.model.user.UserUi
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.model.settings.Language

data class SettingsState(
    val versionInfo: String = "",
    val session: GameSession? = null,
    val isAuth: Boolean = false,
    val user: UserUi? = null,
    val isLogoutDialogVisible: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val language: Language = Language.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
    val notificationsEnabled: Boolean = false,
    val globalError: String? = null,
    val sessionRemainingSeconds: Int = 0,
    val isSessionLoading: Boolean = false,
    val blockedUsersCount: Int = 0,
) {
    val sessionFormattedTime: String get() = if (user?.sessionId != null) {
        "%02d:%02d".format(sessionRemainingSeconds / 60, sessionRemainingSeconds % 60)
    } else ""
}
