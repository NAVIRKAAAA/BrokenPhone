package com.broken.telephone.features.sign_in.use_case

import com.broken.telephone.domain.auth.SignInResult
import com.broken.telephone.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): SignInResult {
        return authRepository.signIn(email.trim(), password)
    }
}
