package com.brokentelephone.game.features.sign_up

import android.util.Patterns
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.auth.PasswordsDoNotMatchException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.brokentelephone.game.essentials.exceptions.main.AppException

class SignUpValidator {

    fun validate(email: String, password: String, confirmPassword: String): List<AppException> {
        return buildList {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) add(InvalidEmailException())
            if (password.length < MIN_PASSWORD_LENGTH) add(WeakPasswordException())
            if (password != confirmPassword) add(PasswordsDoNotMatchException())
        }
    }

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }
}
