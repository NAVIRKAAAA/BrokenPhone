package com.broken.telephone.features.sign_up.use_case

import com.broken.telephone.domain.auth.SignUpResult
import com.broken.telephone.domain.repository.AuthRepository
import com.broken.telephone.features.sign_up.SignUpValidator

class SignUpUseCase(
    private val authRepository: AuthRepository,
    private val signUpValidator: SignUpValidator,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): SignUpResult {
        if (password != confirmPassword) {
            return SignUpResult.Error.PasswordsDoNotMatch
        }

        val validationError = signUpValidator.validate(email.trim(), password)
        if (validationError != null) return validationError

        return authRepository.signUp(email.trim(), password)
    }
}
