package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.auth.GoogleAuthResult

interface AuthRepository {
    suspend fun signUpWithEmailPassword(email: String, password: String): String
    suspend fun signInWithEmailPassword(email: String, password: String)
    suspend fun signInWithGoogle(idToken: String): GoogleAuthResult
    suspend fun signInAnonymously(): String
    suspend fun sendPasswordResetEmail(email: String)
    suspend fun importSession(code: String)
    suspend fun sendEmailChangeVerification(newEmail: String)
    suspend fun applyEmailChange(oobCode: String)
    suspend fun sendEmailVerification(email: String)
    suspend fun applyEmailVerification(oobCode: String)
}
