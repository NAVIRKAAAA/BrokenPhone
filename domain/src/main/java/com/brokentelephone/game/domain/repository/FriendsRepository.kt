package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.friend.FriendRequest
import com.brokentelephone.game.domain.model.friend.FriendshipActionState

interface FriendsRepository {

    suspend fun getFriendshipActionState(currentUserId: String, targetUserId: String): FriendshipActionState

    suspend fun sendFriendRequest(senderId: String, receiverId: String)

    suspend fun getSentPendingRequestId(senderId: String, receiverId: String): String?

    suspend fun acceptFriendRequest(requestId: String)

    suspend fun cancelFriendRequest(requestId: String)

    suspend fun declineFriendRequest(requestId: String)

    suspend fun removeFriend(userId: String, friendId: String)

    suspend fun getSentPendingRequests(senderId: String): List<FriendRequest>

    suspend fun getReceivedPendingRequests(receiverId: String): List<FriendRequest>
}
