package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toNotification
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationFilter
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

    override suspend fun getNotifications(
        userId: String,
        filter: NotificationFilter,
    ): List<Notification> {
        val notificationType = when (filter) {
            NotificationFilter.NEWS -> NOTIFICATION_TYPE_NEWS
            NotificationFilter.FRIENDS -> NOTIFICATION_TYPE_FRIEND_REQUEST
            NotificationFilter.CHAIN -> NOTIFICATION_TYPE_CHAIN_INFO
            else -> emptyList<Notification>()
        }
        return try {
            collection
                .whereArrayContains(FIELD_RECEIVERS_IDS, userId)
                .whereEqualTo(FIELD_TYPE, notificationType)
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

    override fun getUnreadNotificationsCount(
        userId: String,
        readNotificationIds: List<String>,
    ): Flow<Int> = callbackFlow {
        val listener = collection
            .whereArrayContains(FIELD_RECEIVERS_IDS, userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val unreadCount = snapshot.documents.count { doc ->
                    val id = doc.getString(FIELD_ID) ?: return@count false
                    id !in readNotificationIds
                }
                trySend(unreadCount)
            }
        awaitClose { listener.remove() }
    }

    private companion object {
        const val FIELD_ID = "id"
        const val FIELD_RECEIVERS_IDS = "receiversIds"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_TYPE = "type"
        const val NOTIFICATION_TYPE_NEWS = "NEWS"
        const val NOTIFICATION_TYPE_FRIEND_REQUEST = "FRIEND_REQUEST"
        const val NOTIFICATION_TYPE_CHAIN_INFO = "CHAIN_INFO"
    }
}
