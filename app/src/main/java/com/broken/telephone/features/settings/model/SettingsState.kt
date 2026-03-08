package com.broken.telephone.features.settings.model

import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.domain.settings.Language

data class SettingsState(
    val versionInfo: String = "",
    val isAuth: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isLogoutLoading: Boolean = false,
    val language: Language = Language.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
    val notificationsEnabled: Boolean = false,
)
