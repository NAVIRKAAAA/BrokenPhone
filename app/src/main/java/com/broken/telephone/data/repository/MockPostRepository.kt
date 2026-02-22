package com.broken.telephone.data.repository

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.repository.PostRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MockPostRepository : PostRepository {

    private val _posts = MutableStateFlow(mockList)

    override fun getPosts(): Flow<List<Post>> = _posts

    override fun getPostById(id: String): Flow<Post?> = _posts.map { list -> list.find { it.id == id } }

    override fun getChainByPostId(postId: String): Flow<List<PostChainEntry>> = flowOf(chainsMockList)

    override suspend fun updatePost(post: Post) {
        _posts.update { list -> list.map { if (it.id == post.id) post else it } }
    }

    override suspend fun createPost(post: Post) {
        _posts.update { list -> list + post }
    }

    override suspend fun deletePost(postId: String) {
        delay(1500)
        _posts.update { list -> list.filter { it.id != postId } }
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
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_2",
                authorName = "Bob",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing1.png"),
                createdAt = System.currentTimeMillis() - 500_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_3",
                authorName = "Charlie",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                content = PostContent.Text("Something about a telephone that was very broken indeed."),
                createdAt = System.currentTimeMillis() - 400_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_4",
                authorName = "Diana",
                avatarUrl = "",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing2.png"),
                createdAt = System.currentTimeMillis() - 300_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_5",
                authorName = "Ethan",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                content = PostContent.Text("A phone? Or maybe a megaphone? Nobody really knew."),
                createdAt = System.currentTimeMillis() - 200_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_6",
                authorName = "Frank",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_6.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing3.png"),
                createdAt = System.currentTimeMillis() - 160_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_7",
                authorName = "Grace",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_7.png",
                content = PostContent.Text("There was a giant trumpet floating above the city."),
                createdAt = System.currentTimeMillis() - 120_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_8",
                authorName = "Henry",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_8.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing4.png"),
                createdAt = System.currentTimeMillis() - 80_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_9",
                authorName = "Ivy",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_9.png",
                content = PostContent.Text("I think I saw a trombone, but it had legs and was running."),
                createdAt = System.currentTimeMillis() - 40_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
            PostChainEntry(
                parentId = "mock-post",
                authorId = "user_10",
                authorName = "Jack",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_10.png",
                content = PostContent.Drawing(imageUrl = "https://example.com/drawing5.png"),
                createdAt = System.currentTimeMillis() - 10_000,
                status = PostStatus.COMPLETED,
                lockedBy = null,
            ),
        )

        val mockInitialList = listOf(
            Post(
                id = "i1",
                authorId = "user_1",
                authorName = "Alice",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                createdAt = System.currentTimeMillis(),
                generation = 0,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    parentId = "i1",
                    authorId = "user_1",
                    authorName = "Alice",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                    content = PostContent.Text("Once upon a time there was a broken telephone..."),
                    createdAt = System.currentTimeMillis(),
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "i2",
                authorId = "user_2",
                authorName = "Bob",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                createdAt = System.currentTimeMillis() - 60_000,
                generation = 0,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    parentId = "i2",
                    authorId = "user_2",
                    authorName = "Bob",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                    content = PostContent.Text("The message got twisted somewhere along the way."),
                    createdAt = System.currentTimeMillis() - 60_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "i3",
                authorId = "user_3",
                authorName = "Charlie",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                createdAt = System.currentTimeMillis() - 120_000,
                generation = 0,
                maxGenerations = 5,
                textTimeLimit = 45,
                drawingTimeLimit = 90,
                currentEntry = PostChainEntry(
                    parentId = "i3",
                    authorId = "user_3",
                    authorName = "Charlie",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                    content = PostContent.Text("Nobody remembered what the original message was."),
                    createdAt = System.currentTimeMillis() - 120_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "i4",
                authorId = "user_4",
                authorName = "Diana",
                avatarUrl = "",
                createdAt = System.currentTimeMillis() - 180_000,
                generation = 0,
                maxGenerations = 8,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    parentId = "i4",
                    authorId = "user_4",
                    authorName = "Diana",
                    avatarUrl = "",
                    content = PostContent.Text("Something about a cat? Or was it a hat?"),
                    createdAt = System.currentTimeMillis() - 180_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "i5",
                authorId = "user_5",
                authorName = "Eve",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                createdAt = System.currentTimeMillis() - 300_000,
                generation = 0,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
                currentEntry = PostChainEntry(
                    parentId = "i5",
                    authorId = "user_5",
                    authorName = "Eve",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                    content = PostContent.Text("There was a giant trumpet floating above the city."),
                    createdAt = System.currentTimeMillis() - 300_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
        )

        val mockList = listOf(
            Post(
                id = "1",
                authorId = "user_1",
                authorName = "Alice",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                createdAt = System.currentTimeMillis(),
                generation = 0,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    parentId = "1",
                    authorId = "user_1",
                    authorName = "Alice",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_1.png",
                    content = PostContent.Text("Once upon a time there was a broken telephone..."),
                    createdAt = System.currentTimeMillis(),
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "2",
                authorId = "user_2",
                authorName = "Bob",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                createdAt = System.currentTimeMillis() - 60_000,
                generation = 10,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    parentId = "2",
                    authorId = "user_2",
                    authorName = "Bob",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_2.png",
                    content = PostContent.Text("The message got twisted somewhere along the way."),
                    createdAt = System.currentTimeMillis() - 60_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "3",
                authorId = "user_3",
                authorName = "Charlie",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                createdAt = System.currentTimeMillis() - 120_000,
                generation = 2,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    parentId = "3",
                    authorId = "user_3",
                    authorName = "Charlie",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_3.png",
                    content = PostContent.Drawing(imageUrl = "https://example.com/drawing1.png"),
                    createdAt = System.currentTimeMillis() - 120_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "4",
                authorId = "user_4",
                authorName = "Diana",
                avatarUrl = "",
                createdAt = System.currentTimeMillis() - 180_000,
                generation = 3,
                maxGenerations = 10,
                textTimeLimit = 45,
                drawingTimeLimit = 90,
                currentEntry = PostChainEntry(
                    parentId = "4",
                    authorId = "user_4",
                    authorName = "Diana",
                    avatarUrl = "",
                    content = PostContent.Text("Nobody remembered what the original message was."),
                    createdAt = System.currentTimeMillis() - 180_000,
                    status = PostStatus.IN_PROGRESS,
                    lockedBy = "user_5",
                ),
            ),
            Post(
                id = "5",
                authorId = "user_6",
                authorName = "Eve",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                createdAt = System.currentTimeMillis() - 300_000,
                generation = 0,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
                currentEntry = PostChainEntry(
                    parentId = "5",
                    authorId = "user_6",
                    authorName = "Eve",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_5.png",
                    content = PostContent.Text("Something about a cat? Or was it a hat?"),
                    createdAt = System.currentTimeMillis() - 300_000,
                    status = PostStatus.AVAILABLE,
                    lockedBy = null,
                ),
            ),
            Post(
                id = "6",
                authorId = "user_7",
                authorName = "Frank",
                avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_6.png",
                createdAt = System.currentTimeMillis() - 600_000,
                generation = 4,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
                currentEntry = PostChainEntry(
                    parentId = "6",
                    authorId = "user_7",
                    authorName = "Frank",
                    avatarUrl = "https://cdn.jsdelivr.net/gh/alohe/avatars/png/vibrent_6.png",
                    content = PostContent.Drawing(imageUrl = "https://example.com/drawing2.png"),
                    createdAt = System.currentTimeMillis() - 600_000,
                    status = PostStatus.COMPLETED,
                    lockedBy = null,
                ),
            ),
        )
    }
}
