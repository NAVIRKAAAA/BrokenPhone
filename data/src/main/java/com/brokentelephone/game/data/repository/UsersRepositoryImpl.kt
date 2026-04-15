package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toUserDto
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.essentials.exceptions.auth.EmailAlreadyInUseException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from

class UsersRepositoryImpl(
    firestore: FirebaseFirestore,
    private val supabase: SupabaseClient,
) : FirestoreRepository(firestore), UsersRepository {

    override val collectionName = "users"

    override suspend fun getUserById(id: String): User? {
        try {
            val snapshot = collection.document(id).getFromServer()
            return snapshot.data?.let { User.fromMap(it) }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getUsersByIds(ids: List<String>): List<User> {
        try {
            return whereInChunked(User.FIELD_ID, ids, User::fromMap)
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        try {
            val snapshot = collection.whereEqualTo(User.FIELD_EMAIL, email).getFromServer()
            return snapshot.documents.firstOrNull()?.data?.let { User.fromMap(it) }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun searchByUsername(query: String): List<User> {
        try {
            val capitalized = query.replaceFirstChar { it.uppercase() }
            val lowercased = query.lowercase()

            fun rangeFilter(value: String) = Filter.and(
                Filter.greaterThanOrEqualTo(User.FIELD_USERNAME, value),
                Filter.lessThanOrEqualTo(User.FIELD_USERNAME, value + "\uF8FF"),
            )

            val snapshot = collection
                .where(
                    Filter.or(
                        rangeFilter(query),
                        rangeFilter(capitalized),
                        rangeFilter(lowercased)
                    )
                )
                .getFromServer()

            return snapshot.documents
                .mapNotNull { it.data?.let { data -> User.fromMap(data) } }
                .distinctBy { it.id }
                .sortedBy { it.createdAt }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun createUser(
        id: String,
        email: String,
        authProvider: AuthProvider,
    ) {
        val now = System.currentTimeMillis()

        val user = User(
            id = id,
            username = "",
            email = email,
            avatarUrl = null,
//            authProvider = authProvider,
            createdAt = now,
            updatedAt = now,
            notifications = NotificationType.entries,
            onboardingStep = OnboardingStep.CHOOSE_AVATAR,
        )

        try {
            supabase.from("users").insert(user.toUserDto())
        } catch (e: RestException) {
            Log.d("LOG_TAG", "createUser(): $e")
            val msg = e.message.orEmpty()
            when {
                msg.contains("duplicate key", ignoreCase = true) ||
                msg.contains("already exists", ignoreCase = true) -> throw EmailAlreadyInUseException()
                else -> throw UnknownAuthException()
            }
        } catch (e: java.io.IOException) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getSuggestedUsers(excludeIds: List<String>): List<User> {
        try {
            val snapshot = collection
                .orderBy(User.FIELD_UPDATED_AT, com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10 + excludeIds.size.toLong())
                .getFromServer()

            return snapshot.documents
                .mapNotNull { it.data?.let { data -> User.fromMap(data) } }
                .filter { it.id !in excludeIds }
                .take(10)
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "Error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "Error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun createUser(id: String, username: String, avatarUrl: String) {
        val now = System.currentTimeMillis()

        val user = User(
            id = id,
            username = username,
            email = "",
            avatarUrl = avatarUrl,
//            authProvider = AuthProvider.GUEST,
            createdAt = now,
            updatedAt = now,
            notifications = NotificationType.entries,
            onboardingStep = OnboardingStep.COMPLETED,
        )

        try {
            supabase.from("users").insert(user.toUserDto())
        } catch (e: RestException) {
            Log.d("LOG_TAG", "createUser(): $e")
            val msg = e.message.orEmpty()
            when {
                msg.contains("duplicate key", ignoreCase = true) ||
                msg.contains("already exists", ignoreCase = true) -> throw EmailAlreadyInUseException()
                else -> throw UnknownAuthException()
            }
        } catch (e: java.io.IOException) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw UnknownAuthException()
        }
    }
}
