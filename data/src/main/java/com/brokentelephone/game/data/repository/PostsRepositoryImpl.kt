package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.dto.PostDto
import com.brokentelephone.game.data.mapper.toChainDto
import com.brokentelephone.game.data.mapper.toPost
import com.brokentelephone.game.data.mapper.toPostDto
import com.brokentelephone.game.domain.model.chain.Chain
import com.brokentelephone.game.domain.model.chain.ChainStatus
import com.brokentelephone.game.domain.model.pagination.PostsPage
import com.brokentelephone.game.domain.model.post.Post
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.post.PostStatus
import com.brokentelephone.game.domain.model.sort.DashboardSort
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.essentials.exceptions.auth.CannotDeletePostException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.PostInProgressException
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.main.AppException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.PostgrestRequestBuilder
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.IOException
import java.util.UUID

class PostsRepositoryImpl(
    private val supabase: SupabaseClient,
) : PostRepository {

    // TODO: to SQL request
    override suspend fun loadInitialPosts(
        pageSize: Int,
        seed: String,
        excludedUserIds: List<String>,
        excludedPostIds: List<String>,
    ): PostsPage {
        try {
            val posts = supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, users!author_id(username, avatar_url)")) {
                    filter {
                        neq("status", PostStatus.COMPLETED.name)
                        if (excludedUserIds.isNotEmpty()) filterNot(
                            "author_id",
                            FilterOperator.IN,
                            "(${excludedUserIds.joinToString(",")})"
                        )
                        if (excludedPostIds.isNotEmpty()) filterNot(
                            "id",
                            FilterOperator.IN,
                            "(${excludedPostIds.joinToString(",")})"
                        )
                    }
//                    order("md5(id::text || '$seed')", Order.ASCENDING)
                    applySorting(DashboardSort.LATEST)
                    range(0L, (pageSize - 1).toLong())
                }
                .decodeList<PostDto>()
                .map { it.toPost() }
            return PostsPage(posts = posts, offset = posts.size, hasMore = posts.size >= pageSize)
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "loadInitialPosts(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "loadInitialPosts(): $e")
            throw UnknownAuthException()
        }
    }

    // TODO: to SQL request
    override suspend fun loadNextPosts(
        offset: Int,
        pageSize: Int,
        seed: String,
        excludedUserIds: List<String>,
        excludedPostIds: List<String>,
    ): PostsPage {
        try {
            val posts = supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, users!author_id(username, avatar_url)")) {
                    filter {
                        neq("status", PostStatus.COMPLETED.name)
                        if (excludedUserIds.isNotEmpty()) filterNot(
                            "author_id",
                            FilterOperator.IN,
                            "(${excludedUserIds.joinToString(",")})"
                        )
                        if (excludedPostIds.isNotEmpty()) filterNot(
                            "id",
                            FilterOperator.IN,
                            "(${excludedPostIds.joinToString(",")})"
                        )
                    }
//                    order("md5(id::text || '$seed')", Order.ASCENDING)
                    applySorting(DashboardSort.LATEST)
                    range(offset.toLong(), (offset + pageSize - 1).toLong())
                }
                .decodeList<PostDto>()
                .map { it.toPost() }
            return PostsPage(
                posts = posts,
                offset = offset + posts.size,
                hasMore = posts.size >= pageSize
            )
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "loadNextPosts(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "loadNextPosts(): $e")
            throw UnknownAuthException()
        }
    }

    override fun getPostById(id: String): Flow<Post> = callbackFlow {
        Log.d("LOG_TAG", "getPostById(): $id")
        val channel = supabase.realtime.channel("post-$id-${UUID.randomUUID()}")

        val updateFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
            table = TABLE_POSTS
            filter(FilterOperation("id", FilterOperator.EQ, id))
        }

        val collectJob = launch {
            updateFlow.collect { change ->
                try {
                    Log.d("LOG_TAG", "getPostById change: $change")
                    trySend(change.decodeRecord<PostDto>().toPost())
                } catch (_: Exception) {
                }
            }
        }

        channel.subscribe()

        try {
            val post = supabase.from(TABLE_POSTS)
                .select { filter { eq("id", id) } }
                .decodeSingleOrNull<PostDto>()
                ?.toPost()
            if (post == null) {
                close(PostNotFoundException())
                return@callbackFlow
            }
            Log.d("LOG_TAG", "First Sent: $post")
            trySend(post)
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getPostById: $e")
            close(PostNotFoundException())
        }

        awaitClose {
            Log.d("LOG_TAG", "getPostById: awaitClose")
            collectJob.cancel()
            launch { supabase.realtime.removeChannel(channel) }
        }
    }

    override suspend fun getChainByPostId(postId: String): List<Post> {
        try {
            val post = supabase.from(TABLE_POSTS)
                .select { filter { eq("id", postId) } }
                .decodeSingleOrNull<PostDto>()
                ?.toPost() ?: throw PostNotFoundException()

            return supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, users!author_id(username, avatar_url)")) {
                    filter { eq("chain_id", post.chainId) }
                    order("created_at", Order.ASCENDING)
                }
                .decodeList<PostDto>()
                .map { it.toPost() }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getChainByPostId(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getChainByPostId(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getChainById(chainId: String): List<Post> {
        try {
            return supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, users!author_id(username, avatar_url)")) {
                    filter { eq("chain_id", chainId) }
                    order("created_at", Order.ASCENDING)
                }
                .decodeList<PostDto>()
                .map { it.toPost() }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getChainByPostId(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getChainByPostId(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun loadUserCompletedPosts(userId: String): List<Post> {
        try {
            return supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, chains!chain_id!inner(generation)")) {
                    filter {
                        eq("author_id", userId)
                        eq("generation", 0)
                        eq("chains.status", ChainStatus.COMPLETED.name)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<PostDto>()
                .map { postDto ->
                    postDto.toPost().copy(generation = postDto.chain?.generation ?: postDto.generation)
                }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "loadUserCompletedPosts(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "loadUserCompletedPosts(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun loadUserCompletedContributions(userId: String): List<Post> {
        try {
            return supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, chains!chain_id!inner(generation)")) {
                    filter {
                        eq("author_id", userId)
                        gt("generation", 0)
                        eq("chains.status", ChainStatus.COMPLETED.name)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<PostDto>()
                .map { postDto ->
                    postDto.toPost().copy(chainSize = postDto.chain?.generation)
                }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "loadUserCompletedContributions(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "loadUserCompletedContributions(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun loadUserPosts(userId: String): List<Post> {
        try {
            return supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, chains!chain_id(generation)")) {
                    filter {
                        eq("author_id", userId)
                        eq("generation", 0)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<PostDto>()
                .map { postDto ->
                    postDto.toPost().copy(generation = postDto.chain?.generation ?: postDto.generation)
                }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "loadUserPosts(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "loadUserPosts(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun loadContributions(userId: String): List<Post> {
        try {
            return supabase.from(TABLE_POSTS)
                .select(Columns.raw("*, chains!chain_id(generation)")) {
                    filter {
                        eq("author_id", userId)
                        gt("generation", 0)
                    }
                    order("created_at", Order.DESCENDING)
                }
                .decodeList<PostDto>()
                .map { postDto ->
                    postDto.toPost().copy(chainSize = postDto.chain?.generation)
                }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "loadContributions(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "loadContributions(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun createPost(
        authorId: String,
        authorName: String,
        avatarUrl: String?,
        text: String,
        maxGenerations: Int,
        textTimeLimit: Int,
        drawingTimeLimit: Int,
    ) {
        val chainId = UUID.randomUUID().toString()
        val postId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()

        val chain = Chain(
            id = chainId,
            createdAt = now,
            status = ChainStatus.ACTIVE,
            generation = 0,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
        )
        val post = Post(
            id = postId,
            chainId = chainId,
            authorId = authorId,
            authorName = authorName,
            avatarUrl = avatarUrl,
            content = PostContent.Text(text = text),
            createdAt = now,
            updatedAt = now,
            status = PostStatus.AVAILABLE,
            generation = 0,
            maxGenerations = maxGenerations,
            textTimeLimit = textTimeLimit,
            drawingTimeLimit = drawingTimeLimit,
            chainSize = null
        )

        try {
            supabase.from(TABLE_CHAINS).insert(chain.toChainDto())
            supabase.from(TABLE_POSTS).insert(post.toPostDto())
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "createPost(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "createPost(): $e")
            throw UnknownAuthException()
        }
    }

    // TODO: FIX and to sql !!!
    override suspend fun deletePost(postId: String) {
        try {
            val post = supabase.from(TABLE_POSTS)
                .select { filter { eq("id", postId) } }
                .decodeSingleOrNull<PostDto>()
                ?.toPost() ?: throw PostNotFoundException()

            val chainGeneration = supabase.from(TABLE_POSTS)
                .select { filter { eq("chain_id", post.chainId) } }
                .decodeList<PostDto>()
                .maxOfOrNull { it.generation } ?: throw PostNotFoundException()

            if (post.status == PostStatus.IN_PROGRESS) throw PostInProgressException()
            if (chainGeneration != post.generation) throw CannotDeletePostException()
            if (chainGeneration == post.maxGenerations) throw CannotDeletePostException()

            if (post.generation == 0) {
                val chainPostIds = supabase.from(TABLE_POSTS)
                    .select { filter { eq("chain_id", post.chainId) } }
                    .decodeList<PostDto>()
                    .map { it.id }
                chainPostIds.forEach { id ->
                    supabase.from(TABLE_SESSIONS).delete { filter { eq("post_id", id) } }
                }
                supabase.from(TABLE_CHAINS).delete { filter { eq("id", post.chainId) } }
            } else {
                val prevPost = supabase.from(TABLE_POSTS)
                    .select {
                        filter {
                            eq("chain_id", post.chainId)
                            eq("generation", post.generation - 1)
                        }
                    }
                    .decodeSingleOrNull<PostDto>() ?: throw PostNotFoundException()

                supabase.from(TABLE_SESSIONS).delete { filter { eq("post_id", postId) } }
                supabase.from(TABLE_POSTS).delete { filter { eq("id", postId) } }
                supabase.from(TABLE_POSTS).update(
                    buildJsonObject { put("status", PostStatus.AVAILABLE.name) }
                ) { filter { eq("id", prevPost.id) } }
                supabase.from(TABLE_CHAINS).update(
                    buildJsonObject { put("generation", post.generation - 1) }
                ) { filter { eq("id", post.chainId) } }
            }
        } catch (e: AppException) {
            throw e
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "deletePost(): $e")
            throw UnknownAuthException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "deletePost: $e")
            throw UnknownAuthException()
        }
    }

    private fun PostgrestRequestBuilder.applySorting(sort: DashboardSort) {
        when (sort) {
            DashboardSort.LATEST -> order("updated_at", Order.DESCENDING)
            DashboardSort.ALMOST_DONE -> order("generation", Order.DESCENDING)
            DashboardSort.LONGEST_CHAIN -> order("max_generations", Order.DESCENDING)
        }
    }

    private companion object {
        const val TABLE_CHAINS = "chains"
        const val TABLE_POSTS = "posts"
        const val TABLE_SESSIONS = "sessions"
    }
}
