package com.brokentelephone.game.domain.user

import androidx.annotation.StringRes
import com.brokentelephone.game.R

enum class AuthProvider(
    @param:StringRes val labelResId: Int,
) {
    EMAIL(R.string.account_settings_provider_email),
    GOOGLE(R.string.account_settings_provider_google),
    GUEST(R.string.account_settings_provider_guest),
}
