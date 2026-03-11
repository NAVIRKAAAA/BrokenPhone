package com.brokentelephone.game.features.sign_up.use_case

import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.brokentelephone.game.features.sign_up.SignUpValidator

class ValidateSignUpUseCase(
    private val validator: SignUpValidator,
) {
    operator fun invoke(email: String, password: String, confirmPassword: String): List<AppException> {
        return validator.validate(email, password, confirmPassword)
    }
}
