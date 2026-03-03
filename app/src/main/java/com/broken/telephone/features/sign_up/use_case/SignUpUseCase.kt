package com.broken.telephone.features.sign_up.use_case

import com.broken.telephone.domain.auth.SignUpResult
import com.broken.telephone.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        confirmPassword: String,
    ): SignUpResult {
        return authRepository.signUp(email.trim(), password)
    }
}
