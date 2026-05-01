package com.brokentelephone.game.features.new_password.use_case

import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.brokentelephone.game.essentials.validation.SignUpValidator

class ValidateNewPasswordUseCase(
    private val validator: SignUpValidator,
) {
    operator fun invoke(password: String, confirmPassword: String): List<AppException> {
        return validator.validatePassword(password, confirmPassword)
    }
}
