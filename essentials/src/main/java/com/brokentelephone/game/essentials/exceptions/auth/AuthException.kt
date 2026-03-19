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

class ImageUploadException : AppException("Failed to upload image") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_image_upload)
}

class PostNotFoundException : AppException("Post not found") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_post_not_found)
}

class UserNotFoundException : AppException("User not found") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_user_not_found)
}

class AlreadyReportedException : AppException("Post already reported") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_already_reported)
}

class PostInProgressException : AppException("Post is currently in progress") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_post_in_progress)
}

class CannotDeletePostException : AppException("Post cannot be deleted") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_cannot_delete_post)
}

class PostUnavailableToJoinException : AppException("Post is already in progress and cannot be joined") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_post_unavailable_to_join)
}

class SessionNotFoundException : AppException("No active session found for this user") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_session_not_found)
}

class SessionExpiredException : AppException("Session has expired — time limit exceeded") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_session_expired)
}

class SessionValidationException : AppException("Session validation failed — mismatched user, post, or sessionId") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_session_validation)
}

class SessionCooldownException : AppException("Session cooldown active — user recently cancelled this post") {
    override fun getLocalizedMessage(stringProvider: StringProvider): String =
        stringProvider.getString(R.string.error_session_cooldown)
}
