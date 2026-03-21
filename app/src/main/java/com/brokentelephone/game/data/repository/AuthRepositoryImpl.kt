package com.brokentelephone.game.data.repository

import android.content.Context
import android.util.Log
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.essentials.exceptions.auth.EmailAlreadyInUseException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidActionCodeException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidCredentialsException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.RecentLoginRequiredException
import com.brokentelephone.game.essentials.exceptions.auth.TooManyRequestsException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.actionCodeSettings
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context,
) : AuthRepository {

    override suspend fun signUpWithEmailPassword(email: String, password: String): String {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw UnknownAuthException()

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

    override suspend fun signInAnonymously(): String {
        try {
            val authResult = firebaseAuth.signInAnonymously().await()
            return authResult.user?.uid ?: throw UnknownAuthException()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: FirebaseTooManyRequestsException) {
            throw TooManyRequestsException()
        } catch (e: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        try {
            firebaseAuth.sendPasswordResetEmail(email).await()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            throw InvalidEmailException()
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

    override suspend fun sendEmailChangeVerification(newEmail: String) {
        try {
            val user = firebaseAuth.currentUser ?: throw UnauthorizedException()
            val actionCodeSettings = actionCodeSettings {
                url = "https://brokentelephone.firebaseapp.com"
                handleCodeInApp = true
                setAndroidPackageName(
                    context.packageName,
                    true, // installIfNotAvailable
                    null
                )
            }

            user.verifyBeforeUpdateEmail(newEmail, actionCodeSettings).await()
        } catch (_: FirebaseAuthInvalidUserException) {
            throw RecentLoginRequiredException()
        } catch (_: FirebaseAuthRecentLoginRequiredException) {
            throw RecentLoginRequiredException()
        } catch (_: FirebaseAuthUserCollisionException) {
            throw EmailAlreadyInUseException()
        } catch (_: FirebaseAuthInvalidCredentialsException) {
            throw InvalidEmailException()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: FirebaseTooManyRequestsException) {
            throw TooManyRequestsException()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("LOG_TAG", "e: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun applyEmailChange(oobCode: String) {
        try {
            firebaseAuth.applyActionCode(oobCode).await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (_: FirebaseTooManyRequestsException) {
            throw TooManyRequestsException()
        } catch (_: Exception) {
            throw InvalidActionCodeException()
        }
    }
}
