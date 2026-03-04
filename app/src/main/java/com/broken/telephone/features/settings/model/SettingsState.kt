package com.broken.telephone.features.settings.model

data class SettingsState(
    val versionInfo: String = "",
    val isAuth: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isLogoutLoading: Boolean = false,
)
