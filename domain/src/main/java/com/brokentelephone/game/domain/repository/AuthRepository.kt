package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.auth.GoogleAuthResult

interface AuthRepository {

    // Sign Up
    suspend fun signUpWithEmailPassword(email: String, password: String)

    // Sign In
    suspend fun signInWithEmailPassword(email: String, password: String)
    suspend fun signInAnonymously(): String

    // Google
    suspend fun signInWithGoogle(idToken: String): GoogleAuthResult

    suspend fun sendPasswordResetEmail(email: String)
    suspend fun importSession(code: String)
    suspend fun sendEmailChangeVerification(newEmail: String)
    suspend fun applyEmailChange(oobCode: String)
    suspend fun sendEmailVerification(email: String)
    suspend fun applyEmailVerification(oobCode: String)
    suspend fun verifyEmailOtp(email: String, otp: String)
    suspend fun resendSignUpConfirmation(email: String)

    // TODO: migration to UserSessionImpl !!!
    suspend fun getCurrentUserId(): String
    suspend fun getCurrentUserEmail(): String
}
