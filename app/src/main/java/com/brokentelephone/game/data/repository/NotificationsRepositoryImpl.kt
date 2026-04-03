package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toNotification
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class NotificationsRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository(firestore), NotificationsRepository {

    override val collectionName = "notifications"

    override suspend fun getNotifications(userId: String): List<Notification> {
        return try {
            collection
                .whereArrayContains(FIELD_RECEIVERS_IDS, userId)
                .orderBy(FIELD_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()
                .documents
                .mapNotNull { it.data?.toNotification() }
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "getNotifications error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getNotifications error: $e")
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val FIELD_RECEIVERS_IDS = "receiversIds"
        const val FIELD_CREATED_AT = "createdAt"
    }
}
