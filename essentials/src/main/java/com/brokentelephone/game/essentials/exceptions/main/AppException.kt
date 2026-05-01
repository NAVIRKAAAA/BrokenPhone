package com.brokentelephone.game.essentials.exceptions.main

import com.brokentelephone.game.essentials.R
import com.brokentelephone.game.essentials.exceptions.main.string_provider.StringProvider

abstract class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    abstract fun getLocalizedMessage(stringProvider: StringProvider): String

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
