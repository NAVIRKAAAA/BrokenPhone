package com.broken.telephone.features.sign_up

import android.util.Patterns
import com.broken.telephone.domain.auth.SignUpResult

class SignUpValidator {

    fun validate(email: String, password: String): SignUpResult.Error? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return SignUpResult.Error.InvalidEmail
        }

        if (password.length < MIN_PASSWORD_LENGTH) {
            return SignUpResult.Error.PasswordTooShort
        }

        return null
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}
