package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.repository.AuthRepository
import kotlinx.coroutines.delay

class MockAuthRepository : AuthRepository {

    override suspend fun signUpWithEmailPassword(email: String, password: String): String {
        delay(MOCK_DELAY_MS)

        return ""
    }

    override suspend fun signInWithEmailPassword(email: String, password: String) {
        delay(MOCK_DELAY_MS)
    }

    override suspend fun signInAnonymously(): String {
        delay(MOCK_DELAY_MS)
        return "mock-guest-uid"
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        return
    }

    companion object {
        private const val MOCK_DELAY_MS = 1_500L
    }
}
