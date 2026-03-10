package com.brokentelephone.game.core.browser

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

fun Context.openCustomTab(url: String) {
    val activityContext = generateSequence(this) { (it as? ContextWrapper)?.baseContext }
        .firstOrNull { it is Activity } ?: this
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
        .build()
        .launchUrl(activityContext, url.toUri())
}
