package com.broken.telephone.core.browser

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun Context.openCustomTab(url: String) {
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
        .build()
        .launchUrl(this, url.toUri())
}
