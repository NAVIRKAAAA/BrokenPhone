package com.brokentelephone.game.domain.settings

import androidx.annotation.StringRes
import com.brokentelephone.game.R

enum class Language(@param:StringRes val displayNameResId: Int) {
    ENGLISH(R.string.language_english),
    UKRAINIAN(R.string.language_ukrainian),
}
