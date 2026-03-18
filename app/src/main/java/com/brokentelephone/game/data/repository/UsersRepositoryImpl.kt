package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.AuthProvider
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class UsersRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository(firestore), UsersRepository {

    override val collectionName = "users"

    override suspend fun getUserById(id: String): User? {
        try {
            val snapshot = collection.document(id).get().await()
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
            authProvider = authProvider,
            createdAt = now,
            updatedAt = now,
            notifications = NotificationType.entries,
            onboardingStep = OnboardingStep.CHOOSE_AVATAR,
        )

        try {
            collection
                .document(id)
                .set(user.toMap())
                .await()
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
}
