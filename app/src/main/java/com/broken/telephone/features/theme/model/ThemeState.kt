package com.broken.telephone.features.theme.model

import com.broken.telephone.domain.settings.AppTheme

data class ThemeState(
    val selectedTheme: AppTheme = AppTheme.SYSTEM,
)
