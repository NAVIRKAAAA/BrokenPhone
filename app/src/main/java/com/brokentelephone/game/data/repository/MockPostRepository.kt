package com.brokentelephone.game.data.repository

import com.brokentelephone.game.data.model.PostsPage
import com.brokentelephone.game.domain.post.Post
import com.brokentelephone.game.domain.post.PostChainEntry
import com.brokentelephone.game.domain.post.PostContent
import com.brokentelephone.game.domain.post.PostStatus
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.features.dashboard.model.DashboardSort
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockPostRepository : PostRepository {

    override suspend fun loadInitialPosts(
        pageSize: Int,
        sort: DashboardSort,
        userId: String,
        blockedUsersIds: List<String>
    ): PostsPage {
        return PostsPage(listOf(), null)
    }

    override suspend fun loadNextPosts(
        afterDoc: DocumentSnapshot,
        pageSize: Int,
        sort: DashboardSort,
        userId: String,
        blockedUsersIds: List<String>
    ): PostsPage {
        return PostsPage(listOf(), null)
    }

    override fun getPostById(id: String): Flow<Post> = flow { throw PostNotFoundException() }

    override suspend fun getChainByPostId(postId: String): List<Post> = listOf()

    override suspend fun loadUserPosts(userId: String): List<Post> = listOf()

    override suspend fun loadContributions(userId: String): List<Post> = listOf()

    override suspend fun submitContinuation(
        postId: String,
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        content: PostContent
    ) = Unit

    override suspend fun createPost(
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ) {
        val now = System.currentTimeMillis()
        val postId = now.toString()
        Post(
            id = postId,
            parentId = null,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = PostContent.Text(text = text),
            createdAt = now,
            updatedAt = now,
            status = PostStatus.AVAILABLE,
            generation = 1,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
        )
    }

    override suspend fun deletePost(postId: String) {
       return
    }

    companion object {
        val chainsMockList = listOf(
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_1",
                authorName = "Alice",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                content = PostContent.Text("Once upon a time there was a broken telephone..."),
                createdAt = System.currentTimeMillis() - 600_000,
                updatedAt = System.currentTimeMillis() - 600_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_2",
                authorName = "Bob",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing1.png"),
                createdAt = System.currentTimeMillis() - 500_000,
                updatedAt = System.currentTimeMillis() - 500_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_3",
                authorName = "Charlie",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                content = PostContent.Text("Something about a telephone that was very broken indeed."),
                createdAt = System.currentTimeMillis() - 400_000,
                updatedAt = System.currentTimeMillis() - 400_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_4",
                authorName = "Diana",
                avatarUrl = "",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing2.png"),
                createdAt = System.currentTimeMillis() - 300_000,
                updatedAt = System.currentTimeMillis() - 300_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_5",
                authorName = "Ethan",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                content = PostContent.Text("A phone? Or maybe a megaphone? Nobody really knew."),
                createdAt = System.currentTimeMillis() - 200_000,
                updatedAt = System.currentTimeMillis() - 200_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_6",
                authorName = "Frank",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_6.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing3.png"),
                createdAt = System.currentTimeMillis() - 160_000,
                updatedAt = System.currentTimeMillis() - 160_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_7",
                authorName = "Grace",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_7.png",
                content = PostContent.Text("There was a giant trumpet floating above the city."),
                createdAt = System.currentTimeMillis() - 120_000,
                updatedAt = System.currentTimeMillis() - 120_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_8",
                authorName = "Henry",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_8.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing4.png"),
                createdAt = System.currentTimeMillis() - 80_000,
                updatedAt = System.currentTimeMillis() - 80_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_9",
                authorName = "Ivy",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_9.png",
                content = PostContent.Text("I think I saw a trombone, but it had legs and was running."),
                createdAt = System.currentTimeMillis() - 40_000,
                updatedAt = System.currentTimeMillis() - 40_000,
                status = PostStatus.COMPLETED,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_10",
                authorName = "Jack",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_10.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing5.png"),
                createdAt = System.currentTimeMillis() - 10_000,
                updatedAt = System.currentTimeMillis() - 10_000,
                status = PostStatus.COMPLETED,
            ),
        )

        val mockList = listOf(
            Post(
                id = "1",
                parentId = null,
                authorId = "user_1",
                authorName = "Alice",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                content = PostContent.Text("Once upon a time there was a broken telephone..."),
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                status = PostStatus.AVAILABLE,
                generation = 1,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
            ),
            Post(
                id = "2",
                parentId = null,
                authorId = "user_2",
                authorName = "Bob",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                content = PostContent.Text("The message got twisted somewhere along the way."),
                createdAt = System.currentTimeMillis() - 60_000,
                updatedAt = System.currentTimeMillis() - 60_000,
                status = PostStatus.AVAILABLE,
                generation = 10,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
            ),
            Post(
                id = "3",
                parentId = null,
                authorId = "user_3",
                authorName = "Charlie",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing1.png"),
                createdAt = System.currentTimeMillis() - 120_000,
                updatedAt = System.currentTimeMillis() - 120_000,
                status = PostStatus.AVAILABLE,
                generation = 2,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
            ),
            Post(
                id = "4",
                parentId = null,
                authorId = "user_4",
                authorName = "Diana",
                avatarUrl = "",
                content = PostContent.Text("Nobody remembered what the original message was."),
                createdAt = System.currentTimeMillis() - 180_000,
                updatedAt = System.currentTimeMillis() - 180_000,
                status = PostStatus.IN_PROGRESS,
                generation = 3,
                maxGenerations = 10,
                textTimeLimit = 45,
                drawingTimeLimit = 90,
            ),
            Post(
                id = "5",
                parentId = null,
                authorId = "user_6",
                authorName = "Eve",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                content = PostContent.Text("Something about a cat? Or was it a hat?"),
                createdAt = System.currentTimeMillis() - 300_000,
                updatedAt = System.currentTimeMillis() - 300_000,
                status = PostStatus.AVAILABLE,
                generation = 1,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
            ),
            Post(
                id = "6",
                parentId = null,
                authorId = "user_7",
                authorName = "Frank",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_6.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing2.png"),
                createdAt = System.currentTimeMillis() - 600_000,
                updatedAt = System.currentTimeMillis() - 600_000,
                status = PostStatus.COMPLETED,
                generation = 4,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
            ),
        )
    }
}
