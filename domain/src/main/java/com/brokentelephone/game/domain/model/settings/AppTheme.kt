package com.brokentelephone.game.domain.model.settings

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class AppTheme(@param:StringRes val displayNameResId: Int) {
    SYSTEM(R.string.theme_system),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
}
