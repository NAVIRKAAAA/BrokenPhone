package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.essentials.exceptions.auth.EmailAlreadyInUseException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidCredentialsException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.TooManyRequestsException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun signUpWithEmailPassword(email: String, password: String): String {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw NullPointerException()

            uid
        } catch (_: FirebaseAuthUserCollisionException) {
            throw EmailAlreadyInUseException()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            throw InvalidEmailException()
        } catch (_: FirebaseAuthWeakPasswordException) {
            throw WeakPasswordException()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: FirebaseTooManyRequestsException) {
            throw TooManyRequestsException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun signInWithEmailPassword(email: String, password: String) {
        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            throw InvalidCredentialsException()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: FirebaseTooManyRequestsException) {
            throw TooManyRequestsException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }
}
