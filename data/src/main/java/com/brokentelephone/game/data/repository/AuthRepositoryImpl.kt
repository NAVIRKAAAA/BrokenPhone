package com.brokentelephone.game.data.repository

import com.brokentelephone.game.domain.model.auth.GoogleAuthResult
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.essentials.exceptions.auth.EmailAlreadyInUseException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidActionCodeException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidCredentialsException
import com.brokentelephone.game.essentials.exceptions.auth.InvalidEmailException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.TooManyRequestsException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException

class AuthRepositoryImpl(
    private val supabase: SupabaseClient,
) : AuthRepository {

    override suspend fun signUpWithEmailPassword(email: String, password: String): String {
        return try {
            supabase.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            supabase.auth.currentUserOrNull()?.id ?: throw UnknownAuthException()
        } catch (e: RestException) {
            val msg = e.message.orEmpty()
            when {
                msg.contains("already registered", ignoreCase = true) ||
                msg.contains("user_already_exists", ignoreCase = true) -> throw EmailAlreadyInUseException()
                msg.contains("weak_password", ignoreCase = true) ||
                msg.contains("at least", ignoreCase = true) -> throw WeakPasswordException()
                msg.contains("valid email", ignoreCase = true) ||
                msg.contains("invalid_email", ignoreCase = true) -> throw InvalidEmailException()
                msg.contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (e: UnknownAuthException) {
            throw UnknownAuthException()
        } catch (e: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun signInWithEmailPassword(email: String, password: String) {
        try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        } catch (error: RestException) {
            when {
                error.message.orEmpty().contains("Invalid login credentials", ignoreCase = true) -> throw InvalidCredentialsException()
                error.message.orEmpty().contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun signInAnonymously(): String {
        try {
            supabase.auth.signInAnonymously()
            return supabase.auth.currentUserOrNull()?.id ?: throw UnknownAuthException()
        } catch (_: UnknownAuthException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun signInWithGoogle(idToken: String): GoogleAuthResult {
        return try {
            supabase.auth.signInWith(IDToken) {
                provider = Google
                this.idToken = idToken
            }
            val user = supabase.auth.currentUserOrNull() ?: throw UnknownAuthException()
            val createdAt = user.createdAt?.epochSeconds
            val lastSignInAt = user.lastSignInAt?.epochSeconds
            val isNewUser = createdAt == lastSignInAt

            GoogleAuthResult(uid = user.id, email = user.email.orEmpty(), isNewUser = isNewUser)
        } catch (_: UnknownAuthException) {
            throw UnknownAuthException()
        } catch (error: RestException) {
            when {
                error.message.orEmpty().contains("already registered", ignoreCase = true) -> throw EmailAlreadyInUseException()
                error.message.orEmpty().contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun importSession(code: String) {
        try {
            supabase.auth.exchangeCodeForSession(code)
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        try {
            supabase.auth.resetPasswordForEmail(
                email = email,
                redirectUrl = "com.brokentelephone.game://reset-password"
            )
        } catch (error: RestException) {
            when {
                error.message.orEmpty().contains("valid email", ignoreCase = true) ||
                error.message.orEmpty().contains("invalid_email", ignoreCase = true) -> throw InvalidEmailException()
                error.message.orEmpty().contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun sendEmailChangeVerification(newEmail: String) {
        try {
            supabase.auth.updateUser(redirectUrl = "com.brokentelephone.game://change-email") {
                email = newEmail
            }
        } catch (error: RestException) {
            when {
                error.message.orEmpty().contains("already registered", ignoreCase = true) ||
                error.message.orEmpty().contains("already been registered", ignoreCase = true) -> throw EmailAlreadyInUseException()
                error.message.orEmpty().contains("valid email", ignoreCase = true) ||
                error.message.orEmpty().contains("invalid_email", ignoreCase = true) -> throw InvalidEmailException()
                error.message.orEmpty().contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun sendEmailVerification(email: String) {
        try {
            supabase.auth.resendEmail(OtpType.Email.SIGNUP, email)
        } catch (error: RestException) {
            when {
                error.message.orEmpty().contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun applyEmailVerification(oobCode: String) {
        try {
            supabase.auth.exchangeCodeForSession(oobCode)
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw InvalidActionCodeException()
        }
    }

    override suspend fun applyEmailChange(oobCode: String) {
        try {
            supabase.auth.exchangeCodeForSession(oobCode)
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw InvalidActionCodeException()
        }
    }
}
