package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.friend.FriendshipActionState

interface FriendRequestRepository {

    suspend fun getFriendshipActionState(currentUserId: String, targetUserId: String): FriendshipActionState

    suspend fun sendFriendRequest(senderId: String, receiverId: String)

    suspend fun acceptFriendRequest(requestId: String)

    suspend fun cancelFriendRequest(requestId: String)

    suspend fun declineFriendRequest(requestId: String)

    suspend fun removeFriend(userId: String, friendId: String)
}
