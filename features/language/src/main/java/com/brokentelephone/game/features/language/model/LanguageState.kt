package com.brokentelephone.game.features.language.model

import com.brokentelephone.game.domain.model.settings.Language

data class LanguageState(
    val selectedLanguage: Language = Language.ENGLISH,
)
