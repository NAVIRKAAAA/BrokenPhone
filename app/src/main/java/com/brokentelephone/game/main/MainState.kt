package com.brokentelephone.game.main

import com.brokentelephone.game.domain.settings.AppTheme
import com.brokentelephone.game.domain.settings.Language

data class MainState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: Language = Language.ENGLISH,
)
