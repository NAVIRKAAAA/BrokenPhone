package com.brokentelephone.game.essentials.exceptions.auth

import com.brokentelephone.game.essentials.R
import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.brokentelephone.game.essentials.exceptions.main.StringProvider

class EmailAlreadyInUseException : AppException("Email already in use") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_email_already_in_use)
}

class InvalidEmailException : AppException("Invalid email") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_invalid_email)
}

class WeakPasswordException : AppException("Password is too weak") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_weak_password)
}

class PasswordsDoNotMatchException : AppException("Passwords do not match") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_passwords_do_not_match)
}

class NetworkException : AppException("No internet connection") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_network)
}

class TooManyRequestsException : AppException("Too many requests") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_too_many_requests)
}

class InvalidCredentialsException : AppException("Invalid credentials") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_invalid_credentials)
}

class UnknownAuthException : AppException("Unknown auth error") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_auth_unknown)
}

class OperationCancelledException : AppException("Operation was cancelled") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_cancelled)
}

class SessionDataException : AppException("Session found but user data could not be loaded") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_session_data)
}

class UnauthorizedException : AppException("User is not authorized") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_unauthorized)
}
