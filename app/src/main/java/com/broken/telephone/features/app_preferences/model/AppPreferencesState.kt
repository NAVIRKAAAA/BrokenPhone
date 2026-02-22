package com.broken.telephone.features.app_preferences.model

import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.domain.settings.Language

data class AppPreferencesState(
    val language: Language = Language.ENGLISH,
    val theme: AppTheme = AppTheme.LIGHT,
)
