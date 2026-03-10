package com.brokentelephone.game.features.language.model

import com.brokentelephone.game.domain.settings.Language

data class LanguageState(
    val selectedLanguage: Language = Language.ENGLISH,
)
