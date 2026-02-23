package com.broken.telephone.features.language.model

import com.broken.telephone.domain.settings.Language

data class LanguageState(
    val selectedLanguage: Language = Language.ENGLISH,
)
