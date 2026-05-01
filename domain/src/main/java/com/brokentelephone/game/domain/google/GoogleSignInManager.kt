package com.brokentelephone.game.domain.google

interface GoogleSignInManager {
    suspend fun getIdToken(): String
    suspend fun clearCredentialState()
}
