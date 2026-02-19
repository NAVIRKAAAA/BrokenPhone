package com.broken.telephone.data.repository

import com.broken.telephone.domain.post.Post
import com.broken.telephone.domain.post.PostChainEntry
import com.broken.telephone.domain.post.PostContent
import com.broken.telephone.domain.post.PostStatus
import com.broken.telephone.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MockPostRepository : PostRepository {

    private val _posts = MutableStateFlow(mockList)

    override fun getPosts(): Flow<List<Post>> = _posts

    override fun getPostById(id: String): Flow<Post?> = _posts.map { list -> list.find { it.id == id } }

    override suspend fun updatePost(post: Post) {
        _posts.update { list -> list.map { if (it.id == post.id) post else it } }
    }

    companion object {
        val mockList = listOf(
            Post(
                id = "1",
                authorId = "user_1",
                authorName = "Alice",
                avatarUrl = null,
                createdAt = System.currentTimeMillis(),
                generation = 0,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    authorId = "user_1",
                    authorName = "Alice",
                    avatarUrl = null,
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
                avatarUrl = null,
                createdAt = System.currentTimeMillis() - 60_000,
                generation = 1,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    authorId = "user_2",
                    authorName = "Bob",
                    avatarUrl = null,
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
                avatarUrl = null,
                createdAt = System.currentTimeMillis() - 120_000,
                generation = 2,
                maxGenerations = 10,
                textTimeLimit = 30,
                drawingTimeLimit = 60,
                currentEntry = PostChainEntry(
                    authorId = "user_3",
                    authorName = "Charlie",
                    avatarUrl = null,
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
                avatarUrl = null,
                createdAt = System.currentTimeMillis() - 180_000,
                generation = 3,
                maxGenerations = 10,
                textTimeLimit = 45,
                drawingTimeLimit = 90,
                currentEntry = PostChainEntry(
                    authorId = "user_4",
                    authorName = "Diana",
                    avatarUrl = null,
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
                avatarUrl = null,
                createdAt = System.currentTimeMillis() - 300_000,
                generation = 0,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
                currentEntry = PostChainEntry(
                    authorId = "user_6",
                    authorName = "Eve",
                    avatarUrl = null,
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
                avatarUrl = null,
                createdAt = System.currentTimeMillis() - 600_000,
                generation = 4,
                maxGenerations = 5,
                textTimeLimit = 15,
                drawingTimeLimit = 30,
                currentEntry = PostChainEntry(
                    authorId = "user_7",
                    authorName = "Frank",
                    avatarUrl = null,
                    content = PostContent.Drawing(imageUrl = "https://example.com/drawing2.png"),
                    createdAt = System.currentTimeMillis() - 600_000,
                    status = PostStatus.COMPLETED,
                    lockedBy = null,
                ),
            ),
        )
    }
}
