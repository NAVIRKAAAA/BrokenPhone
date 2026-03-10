package com.brokentelephone.game.features.sign_up.use_case

import com.brokentelephone.game.domain.auth.SignUpResult
import com.brokentelephone.game.domain.repository.AuthRepository

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
