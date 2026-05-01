package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.dto.FriendRequestDto
import com.brokentelephone.game.data.dto.FriendshipDto
import com.brokentelephone.game.data.mapper.toDomain
import com.brokentelephone.game.data.mapper.toUser
import com.brokentelephone.game.domain.model.friend.FriendRequest
import com.brokentelephone.game.domain.model.friend.FriendRequestStatus
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendsRepository
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.essentials.exceptions.auth.FriendRequestNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.main.AppException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.IOException
import java.util.UUID

class FriendsRepositoryImpl(
    private val supabase: SupabaseClient,
) : FriendsRepository {

    override suspend fun getFriendshipActionState(
        currentUserId: String,
        targetUserId: String,
    ): FriendshipActionState {
        try {
            val result = supabase.postgrest.rpc(
                "get_friendship_state",
                buildJsonObject {
                    put("p_current", currentUserId)
                    put("p_target", targetUserId)
                }
            ).decodeAs<String>()
            return FriendshipActionState.valueOf(result)
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getFriendshipActionState(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getFriendshipActionState(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun sendFriendRequest(senderId: String, receiverId: String) {
        try {
            supabase.from(TABLE_FRIEND_REQUESTS).insert(
                FriendRequestDto(
                    id = UUID.randomUUID().toString(),
                    senderId = senderId,
                    receiverId = receiverId,
                    createdAt = System.currentTimeMillis(),
                    status = FriendRequestStatus.PENDING.name,
                )
            )
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "sendFriendRequest(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "sendFriendRequest(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun acceptFriendRequest(senderId: String, receiverId: String) {
        try {
            supabase.postgrest.rpc(
                "accept_friend_request",
                buildJsonObject {
                    put("p_sender_id", senderId)
                    put("p_receiver_id", receiverId)
                }
            )
        } catch (e: RestException) {
            val msg = e.message.orEmpty()
            throw when {
                "Friend request not found" in msg -> FriendRequestNotFoundException()
                else -> UnknownAuthException()
            }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "acceptFriendRequest(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun cancelFriendRequest(senderId: String, receiverId: String) {
        try {
            supabase.from(TABLE_FRIEND_REQUESTS).delete {
                filter {
                    eq("sender_id", senderId)
                    eq("receiver_id", receiverId)
                }
            }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "cancelFriendRequest(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "cancelFriendRequest(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun declineFriendRequest(senderId: String, receiverId: String) {
        cancelFriendRequest(senderId, receiverId)
    }

    override suspend fun removeFriend(userId: String, friendId: String) {
        try {
            supabase.postgrest.rpc(
                "remove_friend",
                buildJsonObject {
                    put("p_user_id", userId)
                    put("p_friend_id", friendId)
                }
            )
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "removeFriend(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "removeFriend(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getFriends(userId: String): List<User> {
        try {
            return supabase.from("friendships")
                .select(Columns.raw("users!friend_id(*)")) {
                    filter { eq("user_id", userId) }
                }
                .decodeList<FriendshipDto>()
                .map { it.user.toUser() }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getFriends(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getFriends(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getFriendsCount(userId: String): Int {
        try {
            return supabase.from("friendships").select {
                filter { eq("user_id", userId) }
                count(Count.EXACT)
            }.countOrNull()?.toInt() ?: 0
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getFriendsCount(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getFriendsCount(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getSentPendingRequests(senderId: String): List<FriendRequest> {
        try {
            return supabase.from(TABLE_FRIEND_REQUESTS).select {
                filter {
                    eq("sender_id", senderId)
                    eq("status", FriendRequestStatus.PENDING.name)
                }
            }.decodeList<FriendRequestDto>().mapNotNull { it.toDomain() }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getSentPendingRequests(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getSentPendingRequests(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getReceivedPendingRequests(receiverId: String): List<FriendRequest> {
        try {
            return supabase.from(TABLE_FRIEND_REQUESTS).select {
                filter {
                    eq("receiver_id", receiverId)
                    eq("status", FriendRequestStatus.PENDING.name)
                }
            }.decodeList<FriendRequestDto>().mapNotNull { it.toDomain() }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getReceivedPendingRequests(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getReceivedPendingRequests(): $e")
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val TABLE_FRIEND_REQUESTS = "friend_requests"
    }
}
