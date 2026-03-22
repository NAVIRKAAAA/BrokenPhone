package com.brokentelephone.game.core.locale

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

class LocalizedContextWrapper(base: Context, locale: Locale) : ContextWrapper(base) {

    private val localizedResources: Resources = run {
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        base.createConfigurationContext(config).resources
    }

    override fun getResources(): Resources = localizedResources
}

class LanguageManager {
    fun setLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
