package com.brokentelephone.game.essentials.exceptions

import android.content.Context

abstract class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    abstract fun getLocalizedMessage(stringProvider: StringProvider): String

}

interface StringProvider {
    fun getString(resId: Int): String
}

class StringProviderImpl(
    private val context: Context
) : StringProvider {
    override fun getString(resId: Int): String {
        return context.getString(resId)
    }
}

class TestAppException : AppException("Test App Exception") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String {
        return stringProvider.getString(0)
    }
}

interface ExceptionToMessageMapper {
    fun map(exception: Exception): String
}

class ExceptionToMessageMapperImpl(
    private val stringProvider: StringProvider
) : ExceptionToMessageMapper {
    override fun map(exception: Exception): String {
        return if (exception is AppException) {
            exception.getLocalizedMessage(stringProvider)
        } else {
            stringProvider.getString(0)
        }
    }
}