package com.brokentelephone.game.domain.model.settings

import androidx.annotation.StringRes
import com.brokentelephone.game.domain.R

enum class NotificationType(@param:StringRes val displayNameResId: Int) {
    NEWS(R.string.notifications_type_news),
    CHAIN(R.string.notifications_type_chain),
    FRIENDS(R.string.notifications_type_friends);

    companion object {
        fun getByName(value: String): NotificationType? =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
    }
}
