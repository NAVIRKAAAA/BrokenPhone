package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.friend.FriendRequest
import com.brokentelephone.game.domain.model.friend.FriendshipActionState
import com.brokentelephone.game.domain.user.User

interface FriendsRepository {

    suspend fun getFriendshipActionState(currentUserId: String, targetUserId: String): FriendshipActionState

    suspend fun sendFriendRequest(senderId: String, receiverId: String)

    suspend fun acceptFriendRequest(senderId: String, receiverId: String)

    suspend fun cancelFriendRequest(senderId: String, receiverId: String)

    suspend fun declineFriendRequest(senderId: String, receiverId: String)

    suspend fun removeFriend(userId: String, friendId: String)

    suspend fun getFriends(userId: String): List<User>

    suspend fun getFriendsCount(userId: String): Int

    suspend fun getSentPendingRequests(senderId: String): List<FriendRequest>

    suspend fun getReceivedPendingRequests(receiverId: String): List<FriendRequest>
}
