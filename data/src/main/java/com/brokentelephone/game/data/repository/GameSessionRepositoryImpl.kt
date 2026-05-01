package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.dto.GameSessionDto
import com.brokentelephone.game.data.mapper.toGameSession
import com.brokentelephone.game.domain.model.post.PostContent
import com.brokentelephone.game.domain.model.session.GameSession
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.PostNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.PostUnavailableToJoinException
import com.brokentelephone.game.essentials.exceptions.auth.SessionExpiredException
import com.brokentelephone.game.essentials.exceptions.auth.SessionNotFoundException
import com.brokentelephone.game.essentials.exceptions.auth.SessionValidationException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.auth.UserAlreadyInSessionException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
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

class GameSessionRepositoryImpl(
    private val supabase: SupabaseClient,
) : GameSessionRepository {

    override fun getSession(sessionId: String): Flow<GameSession> = callbackFlow {
        val channel = supabase.realtime.channel("session-$sessionId-${UUID.randomUUID()}")

        val updateFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
            table = TABLE_SESSIONS
            filter(FilterOperation("id", FilterOperator.EQ, sessionId))
        }

        val collectJob = launch {
            updateFlow.collect { change ->
                try {
                    trySend(change.decodeRecord<GameSessionDto>().toGameSession())
                } catch (_: Exception) {
                }
            }
        }

        channel.subscribe()

        try {
            val session = supabase.from(TABLE_SESSIONS)
                .select { filter { eq("id", sessionId) } }
                .decodeSingleOrNull<GameSessionDto>()
                ?.toGameSession()
            if (session == null) {
                close(SessionNotFoundException())
                return@callbackFlow
            }
            trySend(session)
        } catch (_: Exception) {
            close(SessionNotFoundException())
        }

        awaitClose {
            collectJob.cancel()
            launch { supabase.realtime.removeChannel(channel) }
        }
    }

    override suspend fun joinSession(postId: String, userId: String): GameSession {
        try {
            return supabase.postgrest.rpc(
                "join_session",
                buildJsonObject {
                    put("p_post_id", postId)
                    put("p_user_id", userId)
                }
            ).decodeAs<GameSessionDto>().toGameSession()
        } catch (e: RestException) {
            Log.d("LOG_TAG", "joinSession: $e")
            val msg = e.message.orEmpty()
            throw when {
                "USER_ALREADY_IN_SESSION" in msg -> UserAlreadyInSessionException()
                "POST_NOT_FOUND" in msg -> PostNotFoundException()
                "POST_UNAVAILABLE" in msg -> PostUnavailableToJoinException()
                else -> UnknownAuthException()
            }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "joinSession: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun cancelSession(sessionId: String, userId: String) {
        try {
            supabase.postgrest.rpc(
                "cancel_session",
                buildJsonObject {
                    put("p_session_id", sessionId)
                    put("p_user_id", userId)
                }
            )
        } catch (e: RestException) {
            Log.d("LOG_TAG", "cancelSession: $e")
            val msg = e.message.orEmpty()
            throw when {
                "SESSION_NOT_FOUND" in msg -> SessionNotFoundException()
                "SESSION_VALIDATION_ERROR" in msg -> SessionValidationException()
                else -> UnknownAuthException()
            }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeSession(
        sessionId: String,
        authorId: String,
        content: PostContent,
    ) {
        try {
            Log.d("LOG_TAG", "completeSession()")
            supabase.postgrest.rpc(
                "complete_session",
                buildJsonObject {
                    put("p_session_id", sessionId)
                    put("p_author_id", authorId)
                    put(
                        "p_content_type", when (content) {
                            is PostContent.Text -> "TEXT"
                            is PostContent.Drawing -> "DRAWING"
                        }
                    )
                    put("p_content_text", (content as? PostContent.Text)?.text)
                    put("p_content_image_url", (content as? PostContent.Drawing)?.imageUrl)
                }
            )
        } catch (e: RestException) {
            Log.d("LOG_TAG", "completeSession: $e")
            val msg = e.message.orEmpty()
            throw when {
                "SESSION_NOT_FOUND" in msg -> SessionNotFoundException()
                "SESSION_VALIDATION_ERROR" in msg -> SessionValidationException()
                "SESSION_EXPIRED" in msg -> SessionExpiredException()
                "POST_NOT_FOUND" in msg -> PostNotFoundException()
                else -> UnknownAuthException()
            }
        } catch (_: IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    private companion object {
        const val TABLE_SESSIONS = "sessions"
    }
}