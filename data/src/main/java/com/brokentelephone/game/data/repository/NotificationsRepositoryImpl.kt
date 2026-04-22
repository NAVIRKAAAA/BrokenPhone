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
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.IOException

class NotificationsRepositoryImpl(
    private val supabase: SupabaseClient,
) : NotificationsRepository {

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

    override fun getUnreadNotificationsCount(
        userId: String,
        readNotificationIds: List<String>,
    ): Flow<Int> = flow {
        try {
            val allIds = supabase.from(TABLE_NOTIFICATIONS)
                .select(columns = Columns.list("id")) {
                    filter { contains("receiver_ids", listOf(userId)) }
                }
                .decodeList<NotificationIdDto>()
                .map { it.id }
            emit(allIds.count { it !in readNotificationIds })
        } catch (_: IOException) {
            emit(0)
        } catch (_: Exception) {
            emit(0)
        }
    }

    @Serializable
    private data class NotificationIdDto(@SerialName("id") val id: String)

    private companion object {
        const val TABLE_NOTIFICATIONS = "notifications"
        const val TYPE_FRIEND_REQUEST = "FRIEND_REQUEST"
        const val TYPE_CHAIN_INFO = "CHAIN_INFO"
        const val TYPE_NEWS = "NEWS"
    }
}
