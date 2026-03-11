package com.brokentelephone.game.features.sign_in.use_case

import com.brokentelephone.game.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) {
        return authRepository.signInWithEmailPassword(email.trim(), password)
    }
}
