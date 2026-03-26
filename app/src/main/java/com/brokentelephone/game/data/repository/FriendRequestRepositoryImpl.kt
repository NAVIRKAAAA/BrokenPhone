package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.mapper.toAppException
import com.brokentelephone.game.data.mapper.toMap
import com.brokentelephone.game.domain.model.friend.FriendRequest
import com.brokentelephone.game.domain.model.friend.FriendRequestStatus
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.repository.FriendRequestRepository
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await

class FriendRequestRepositoryImpl(
    firestore: FirebaseFirestore,
) : FirestoreRepository(firestore), FriendRequestRepository {

    override val collectionName = "friend_requests"

    override suspend fun getFriendshipActionState(
        currentUserId: String,
        targetUserId: String,
    ): FriendshipActionState {
        try {
            val userSnapshot = collection.firestore
                .collection(USERS_COLLECTION)
                .document(currentUserId)
                .get()
                .await()

            val user = userSnapshot.data?.let { User.fromMap(it) }
            val friendIds = user?.friendIds ?: listOf()

            if (targetUserId in friendIds) return FriendshipActionState.FRIENDS

            val sentRequest = collection
                .whereEqualTo(FIELD_SENDER_ID, currentUserId)
                .whereEqualTo(FIELD_RECEIVER_ID, targetUserId)
                .whereEqualTo(FIELD_STATUS, FriendRequestStatus.PENDING.name)
                .get()
                .await()

            if (!sentRequest.isEmpty) return FriendshipActionState.INVITE_SENT

            val receivedRequest = collection
                .whereEqualTo(FIELD_SENDER_ID, targetUserId)
                .whereEqualTo(FIELD_RECEIVER_ID, currentUserId)
                .whereEqualTo(FIELD_STATUS, FriendRequestStatus.PENDING.name)
                .get()
                .await()

            if (!receivedRequest.isEmpty) return FriendshipActionState.INVITE_RECEIVED

            return FriendshipActionState.NOT_FRIENDS
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "getFriendshipActionState error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getFriendshipActionState error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun sendFriendRequest(senderId: String, receiverId: String) {
        try {
            val docRef = collection.document()
            val request = FriendRequest(
                id = docRef.id,
                senderId = senderId,
                receiverId = receiverId,
                createdAt = System.currentTimeMillis(),
                status = FriendRequestStatus.PENDING,
            )
            docRef.set(request.toMap()).await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "sendFriendRequest error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "sendFriendRequest error: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun acceptFriendRequest(requestId: String) {
        return
    }

    override suspend fun cancelFriendRequest(requestId: String) {
        updateStatus(requestId, FriendRequestStatus.CANCELLED_BY_SENDER)
    }

    override suspend fun declineFriendRequest(requestId: String) {
        updateStatus(requestId, FriendRequestStatus.DECLINED_BY_RECEIVER)
    }

    override suspend fun removeFriend(userId: String, friendId: String) {
        return
    }

    private suspend fun updateStatus(requestId: String, status: FriendRequestStatus) {
        try {
            collection.document(requestId)
                .update(FIELD_STATUS, status.name)
                .await()
        } catch (_: FirebaseNetworkException) {
            throw NetworkException()
        } catch (e: FirebaseFirestoreException) {
            Log.d("LOG_TAG", "updateStatus error: $e")
            throw e.toAppException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "updateStatus error: $e")
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val FIELD_STATUS = "status"
        const val FIELD_SENDER_ID = "senderId"
        const val FIELD_RECEIVER_ID = "receiverId"
    }
}
