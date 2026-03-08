package com.broken.telephone.core.locale

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
