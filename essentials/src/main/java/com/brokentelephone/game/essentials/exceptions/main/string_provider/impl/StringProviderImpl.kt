
package com.brokentelephone.game.essentials.exceptions.main.string_provider.impl

import android.content.Context
import com.brokentelephone.game.essentials.exceptions.main.string_provider.StringProvider

internal class StringProviderImpl(
    private val context: Context,
) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}