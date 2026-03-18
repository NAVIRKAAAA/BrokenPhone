package com.brokentelephone.game.features.settings.model

import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.model.settings.Language

data class SettingsState(
    val versionInfo: String = "",
    val isAuth: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val language: Language = Language.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
    val notificationsEnabled: Boolean = false,
    val globalError: String? = null,
    val blockedUsersCount: Int = 0,
)
