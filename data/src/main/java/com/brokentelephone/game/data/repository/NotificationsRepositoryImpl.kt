package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.dto.NotificationDto
import com.brokentelephone.game.data.mapper.toNotification
import com.brokentelephone.game.domain.model.notification.Notification
import com.brokentelephone.game.domain.model.notification.NotificationFilter
import com.brokentelephone.game.domain.repository.NotificationsRepository
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.IOException

class NotificationsRepositoryImpl(
    private val supabase: SupabaseClient,
) : NotificationsRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override suspend fun getNotificationById(notificationId: String): Notification? {
        return try {
            supabase.from(TABLE_NOTIFICATIONS)
                .select { filter { eq("id", notificationId) } }
                .decodeSingleOrNull<NotificationDto>()
                ?.toNotification()
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getNotificationById: $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getNotificationById: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getNotifications(userId: String): List<Notification> {
        return try {
            supabase.from(TABLE_NOTIFICATIONS)
                .select {
                    filter { contains("receiver_ids", listOf(userId)) }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationDto>()
                .mapNotNull { it.toNotification() }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getNotifications: $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getNotifications: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getNotifications(
        userId: String,
        filter: NotificationFilter,
    ): List<Notification> {
        val type = when (filter) {
            NotificationFilter.FRIENDS -> TYPE_FRIEND_REQUEST
            NotificationFilter.CHAIN -> TYPE_CHAIN_INFO
            NotificationFilter.NEWS -> TYPE_NEWS
            NotificationFilter.ALL,
            NotificationFilter.UNREAD -> return getNotifications(userId)
        }
        return try {
            supabase.from(TABLE_NOTIFICATIONS)
                .select {
                    filter {
                        contains("receiver_ids", listOf(userId))
                        eq("type", type)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<NotificationDto>()
                .mapNotNull { it.toNotification() }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getNotifications(filter): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getNotifications(filter): $e")
            throw UnknownAuthException()
        }
    }

    override fun observeNewNotifications(): Flow<Notification> = callbackFlow {
        Log.d(TAG, "Start Observer: observeNewNotifications")
        val channel = supabase.realtime.channel("new-notif-${System.currentTimeMillis()}")

        val insertFlow = channel.postgresChangeFlow<PostgresAction.Insert>(schema = "public") {
            table = TABLE_NOTIFICATIONS
        }

        val job = launch {
            insertFlow.collect { action ->
                Log.d(TAG, "New notification: $action")
                val notification = runCatching {
                    action.decodeRecord<NotificationDto>().toNotification()
                }.getOrNull() ?: return@collect
                send(notification)
            }
        }

        channel.subscribe()
        awaitClose {
            job.cancel()
            scope.launch {
                Log.d(TAG, "Remove Observer: observeNewNotifications")
                supabase.realtime.removeChannel(channel)
            }
        }
    }

    override suspend fun markNotificationAsRead(userId: String, notificationId: String) {
        try {
            supabase.postgrest.rpc(
                "mark_notification_as_read",
                buildJsonObject {
                    put("p_user_id", userId)
                    put("p_notification_id", notificationId)
                }
            )
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "markNotificationAsRead: $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "markNotificationAsRead: $e")
            throw UnknownAuthException()
        }
    }

    private companion object {

        const val TAG = "NotificationsRepositoryImpl"

        const val TABLE_NOTIFICATIONS = "notifications"
        const val TYPE_FRIEND_REQUEST = "FRIEND_REQUEST"
        const val TYPE_CHAIN_INFO = "CHAIN_INFO"
        const val TYPE_NEWS = "NEWS"
    }
}
