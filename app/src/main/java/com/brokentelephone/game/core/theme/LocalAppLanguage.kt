package com.brokentelephone.game.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import com.brokentelephone.game.domain.model.settings.Language

val LocalAppLanguage = staticCompositionLocalOf { Language.ENGLISH }
