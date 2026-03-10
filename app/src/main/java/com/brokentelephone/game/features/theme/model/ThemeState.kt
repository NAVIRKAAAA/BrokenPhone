package com.brokentelephone.game.features.theme.model

import com.brokentelephone.game.domain.settings.AppTheme

data class ThemeState(
    val selectedTheme: AppTheme = AppTheme.SYSTEM,
)
