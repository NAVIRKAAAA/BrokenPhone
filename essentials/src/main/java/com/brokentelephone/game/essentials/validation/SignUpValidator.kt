package com.brokentelephone.game.essentials.validation

import android.util.Patterns
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.auth.PasswordsDoNotMatchException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.brokentelephone.game.essentials.exceptions.main.AppException

class SignUpValidator {

    fun validate(email: String, password: String, confirmPassword: String): List<AppException> {
        return buildList {
            if (!isEmailValid(email)) add(InvalidEmailException())
            if (password.length < MIN_PASSWORD_LENGTH) add(WeakPasswordException())
            if (password != confirmPassword) add(PasswordsDoNotMatchException())
        }
    }

    fun validate(email: String): List<AppException> {
        return buildList {
            if (!isEmailValid(email)) add(InvalidEmailException())
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}