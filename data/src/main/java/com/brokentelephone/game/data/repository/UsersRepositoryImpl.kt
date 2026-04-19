package com.brokentelephone.game.data.repository

import android.util.Log
import com.brokentelephone.game.data.dto.UserDto
import com.brokentelephone.game.data.mapper.toUser
import com.brokentelephone.game.data.mapper.toUserDto
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.essentials.exceptions.auth.EmailAlreadyInUseException
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import java.io.IOException

class UsersRepositoryImpl(
    private val supabase: SupabaseClient,
) : UsersRepository {

    override suspend fun getUserById(id: String): User? {
        try {
            return supabase.from(COLLECTION_USERS)
                .select { filter { eq("id", id) } }
                .decodeSingleOrNull<UserDto>()
                ?.toUser()
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getUserById(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getUsersByIds(ids: List<String>): List<User> {
        if (ids.isEmpty()) return emptyList()
        try {
            return supabase.from(COLLECTION_USERS)
                .select { filter { isIn("id", ids) } }
                .decodeList<UserDto>()
                .map { it.toUser() }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getUsersByIds(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        try {
            return supabase.from(COLLECTION_USERS)
                .select { filter { eq("email", email) } }
                .decodeSingleOrNull<UserDto>()
                ?.toUser()
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getUserByEmail(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun searchByUsername(query: String): List<User> {
        try {
            return supabase.from(COLLECTION_USERS)
                .select { filter { ilike("username", "%$query%") } }
                .decodeList<UserDto>()
                .map { it.toUser() }
                .sortedBy { it.createdAt }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "searchByUsername(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun createUser(
        id: String,
        email: String,
    ) {
        val now = System.currentTimeMillis()

        val user = User(
            id = id,
            username = "",
            email = email,
            avatarUrl = null,
            createdAt = now,
            updatedAt = now,
            notifications = NotificationType.entries,
            onboardingStep = OnboardingStep.CHOOSE_AVATAR,
        )

        try {
            supabase.from(COLLECTION_USERS).insert(user.toUserDto())
        } catch (e: RestException) {
            Log.d("LOG_TAG", "createUser(): $e")
            val msg = e.message.orEmpty()
            when {
                msg.contains("duplicate key", ignoreCase = true) ||
                        msg.contains(
                            "already exists",
                            ignoreCase = true
                        ) -> throw EmailAlreadyInUseException()

                else -> throw UnknownAuthException()
            }
        } catch (e: IOException) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getSuggestedUsers(): List<User> {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()

        try {
            return supabase.postgrest.rpc(
                "get_suggested_users",
                buildJsonObject {
                    put("current_user_id", JsonPrimitive(userId))
                }
            )
                .decodeList<UserDto>()
                .map { it.toUser() }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getSuggestedUsers(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun createUser(id: String, username: String, avatarUrl: String) {
        val now = System.currentTimeMillis()

        val user = User(
            id = id,
            username = username,
            email = "",
            avatarUrl = avatarUrl,
//            authProvider = AuthProvider.GUEST,
            createdAt = now,
            updatedAt = now,
            notifications = NotificationType.entries,
            onboardingStep = OnboardingStep.COMPLETED,
        )

        try {
            supabase.from(COLLECTION_USERS).insert(user.toUserDto())
        } catch (e: RestException) {
            Log.d("LOG_TAG", "createUser(): $e")
            val msg = e.message.orEmpty()
            when {
                msg.contains("duplicate key", ignoreCase = true) ||
                        msg.contains(
                            "already exists",
                            ignoreCase = true
                        ) -> throw EmailAlreadyInUseException()

                else -> throw UnknownAuthException()
            }
        } catch (e: IOException) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "createUser(): $e")
            throw UnknownAuthException()
        }
    }

    companion object {
        private const val COLLECTION_USERS = "users"
    }
}
