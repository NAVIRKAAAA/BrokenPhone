package com.broken.telephone.main

import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.domain.settings.Language

data class MainState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: Language = Language.ENGLISH,
)
