package com.brokentelephone.game.features.edit_email.use_case

import com.brokentelephone.game.essentials.exceptions.main.AppException
import com.brokentelephone.game.essentials.validation.SignUpValidator

class ValidateEmailUseCase(
    private val validator: SignUpValidator,
) {
    fun execute(email: String): List<AppException> {
        return validator.validate(email)
    }
}
