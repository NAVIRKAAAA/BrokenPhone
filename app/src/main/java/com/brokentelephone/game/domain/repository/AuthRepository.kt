package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.auth.SignInResult
import com.brokentelephone.game.domain.auth.SignUpResult

interface AuthRepository {
    suspend fun signUp(email: String, password: String): SignUpResult
    suspend fun signIn(email: String, password: String): SignInResult
}
