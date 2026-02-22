package com.broken.telephone.data.session

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.user.AuthState
import com.broken.telephone.domain.user.BlockedUser
import com.broken.telephone.domain.user.User
import com.broken.telephone.domain.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class MockUserSessionImpl : UserSession {

    private val _authState = MutableStateFlow<AuthState>(
        AuthState.Auth(
            user = User(
                id = "mock-user-id",
                username = "Alex",
                email = "alex@example.com",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                createdAt = 1_700_000_000_000L,
                updatedAt = 1_700_000_000_000L,
            )
        )
    )

    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override suspend fun updateProfile(username: String) {
        val current = _authState.value
        if (current is AuthState.Auth) {
            _authState.value = AuthState.Auth(current.user.copy(username = username, updatedAt = System.currentTimeMillis()))
        }
    }

    override suspend fun updateAvatar(avatarUrl: String) {
        val current = _authState.value
        if (current is AuthState.Auth) {
            _authState.value = AuthState.Auth(current.user.copy(avatarUrl = avatarUrl, updatedAt = System.currentTimeMillis()))
        }
    }

    private val _blockedUsers = MutableStateFlow(
        listOf(
            BlockedUser(id = "block_1", userId = "user_2", createdAt = System.currentTimeMillis() - 86_400_000),
            BlockedUser(id = "block_2", userId = "user_3", createdAt = System.currentTimeMillis() - 172_800_000),
            BlockedUser(id = "block_3", userId = "user_4", createdAt = System.currentTimeMillis() - 259_200_000),
        )
    )

    override fun getBlockedUsers(): Flow<List<BlockedUser>> = _blockedUsers.asStateFlow()

    override suspend fun blockUser(blockedUserId: String) {
        delay(1500)
        val newBlock = BlockedUser(
            id = "block_${System.currentTimeMillis()}",
            userId = blockedUserId,
            createdAt = System.currentTimeMillis(),
        )
        _blockedUsers.update { list -> list + newBlock }
    }

    override suspend fun unblockUser(blockId: String) {
        delay(1500)
        _blockedUsers.update { list -> list.filter { it.id != blockId } }
    }

    override suspend fun logout() {
        delay(1500)
        _authState.value = AuthState.NotAuth
    }

    override suspend fun deleteAccount() {
        delay(1500)
        _authState.value = AuthState.NotAuth
    }

    override fun getMyPosts(): Flow<List<Post>> = flowOf(
        listOf(
            mockPost(
                id = "1",
                authorId = "user_2",
                authorName = "Bob",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                content = PostContent.Text("Something about a mysterious signal."),
                generation = 2,
                maxGenerations = 8,
                status = PostStatus.AVAILABLE,
            ),
            mockPost(
                id = "2",
                authorId = "user_3",
                authorName = "Charlie",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                content = PostContent.Drawing(imageUrl = ""),
                generation = 4,
                maxGenerations = 10,
                status = PostStatus.IN_PROGRESS,
            ),
            mockPost(
                id = "3",
                authorId = "user_4",
                authorName = "Diana",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_4.png",
                content = PostContent.Text("Nobody knew where the sound was coming from."),
                generation = 6,
                maxGenerations = 6,
                status = PostStatus.COMPLETED,
            )
        )
    )

    override fun getMyContributions(): Flow<List<Post>> = flowOf(
        listOf(
            mockPost(
                id = "4",
                authorId = "mock-user-id",
                authorName = "Alex",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                content = PostContent.Text("My first broken telephone story!"),
                generation = 0,
                maxGenerations = 10,
                status = PostStatus.AVAILABLE,
            ),
            mockPost(
                id = "5",
                authorId = "mock-user-id",
                authorName = "Alex",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                content = PostContent.Drawing(imageUrl = ""),
                generation = 3,
                maxGenerations = 5,
                status = PostStatus.IN_PROGRESS,
            ),
            mockPost(
                id = "6",
                authorId = "mock-user-id",
                authorName = "Alex",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                content = PostContent.Text("The final chapter of the telephone saga."),
                generation = 5,
                maxGenerations = 5,
                status = PostStatus.COMPLETED,
            ),
        )
    )

    private fun mockPost(
        id: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent,
        generation: Int,
        maxGenerations: Int,
        status: PostStatus,
    ) = Post(
        id = id,
        authorId = authorId,
        authorName = authorName,
        avatarUrl = avatarUrl,
        createdAt = System.currentTimeMillis(),
        generation = generation,
        maxGenerations = maxGenerations,
        textTimeLimit = 30,
        drawingTimeLimit = 60,
        currentEntry = PostChainEntry(
            parentId = id,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = content,
            createdAt = System.currentTimeMillis(),
            status = status,
            lockedBy = null,
        ),
    )
}
