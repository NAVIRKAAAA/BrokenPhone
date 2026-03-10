package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.auth.SignInResult
import com.brokentelephone.game.domain.auth.SignUpResult
import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.delay

class MockAuthRepository : AuthRepository {

    override suspend fun signUp(email: String, password: String): SignUpResult {
        delay(MOCK_DELAY_MS)
        return SignUpResult.Success
    }

    override suspend fun signIn(email: String, password: String): SignInResult {
        delay(MOCK_DELAY_MS)
        return SignInResult.Success
    }

    companion object {
        private const val MOCK_DELAY_MS = 1_500L
    }
}
