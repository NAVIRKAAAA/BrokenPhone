package com.brokentelephone.game.essentials.exceptions.main

import android.content.Context
import com.brokentelephone.game.essentials.R

abstract class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    abstract fun getLocalizedMessage(stringProvider: StringProvider): String

}

interface StringProvider {
    fun getString(resId: Int): String
}

internal class StringProviderImpl(
    private val context: Context,
) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}

interface ExceptionToMessageMapper {
    fun map(exception: Exception): String
}

internal class ExceptionToMessageMapperImpl(
    private val stringProvider: StringProvider
) : ExceptionToMessageMapper {
    override fun map(exception: Exception): String {
        return if (exception is AppException) {
            exception.getLocalizedMessage(stringProvider)
        } else {
            stringProvider.getString(R.string.error_auth_unknown)
        }
    }
}
