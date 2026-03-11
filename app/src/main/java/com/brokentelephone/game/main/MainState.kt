package com.brokentelephone.game.main

import com.brokentelephone.game.domain.settings.AppTheme
import com.brokentelephone.game.domain.settings.Language
import com.brokentelephone.game.navigation.routes.Routes

data class MainState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val language: Language = Language.ENGLISH,
    val startDestination: Routes? = null,
) {
    val isReady: Boolean get() = startDestination != null
}
