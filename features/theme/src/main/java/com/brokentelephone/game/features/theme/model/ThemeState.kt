package com.brokentelephone.game.features.theme.model

import com.brokentelephone.game.domain.model.settings.AppTheme

data class ThemeState(
    val selectedTheme: AppTheme = AppTheme.SYSTEM,
)
