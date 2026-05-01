package com.brokentelephone.game.domain.model.settings

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R
import java.util.Locale

enum class Language(@param:StringRes val displayNameResId: Int) {
    ENGLISH(R.string.language_english),
    UKRAINIAN(R.string.language_ukrainian),
}

fun Language.toLocale(): Locale = when (this) {
    Language.ENGLISH -> Locale.ENGLISH
    Language.UKRAINIAN -> Locale.forLanguageTag("uk")
}

fun Language.toLanguageTag(): String = toLocale().language
