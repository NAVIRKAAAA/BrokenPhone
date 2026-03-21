package com.brokentelephone.game.domain.repository

interface AuthRepository {
    suspend fun signUpWithEmailPassword(email: String, password: String): String
    suspend fun signInWithEmailPassword(email: String, password: String)
    suspend fun signInAnonymously(): String
    suspend fun sendPasswordResetEmail(email: String)
    suspend fun sendEmailChangeVerification(newEmail: String)
    suspend fun applyEmailChange(oobCode: String)
}
