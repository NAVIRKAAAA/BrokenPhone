package com.broken.telephone.data.repository

import com.broken.telephone.domain.auth.SignUpResult
import com.broken.telephone.domain.repository.AuthRepository
import kotlinx.coroutines.delay

class MockAuthRepository : AuthRepository {

    override suspend fun signUp(email: String, password: String): SignUpResult {
        delay(MOCK_DELAY_MS)
        return SignUpResult.Success
    }

    companion object {
        private const val MOCK_DELAY_MS = 1_500L
    }
}
