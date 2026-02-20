package com.broken.telephone.domain.repository

import com.broken.telephone.domain.auth.SignUpResult

interface AuthRepository {
    suspend fun signUp(email: String, password: String): SignUpResult
}
