package com.brokentelephone.game.data.session

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.brokentelephone.game.data.dto.UserBlockDto
import com.brokentelephone.game.data.dto.UserBlockWithUserDto
import com.brokentelephone.game.data.dto.UserDto
import com.brokentelephone.game.data.dto.toBlockedUser
import com.brokentelephone.game.data.mapper.toUser
import com.brokentelephone.game.domain.model.permissions.UserPermissions
import com.brokentelephone.game.domain.model.settings.NotificationType
import com.brokentelephone.game.domain.user.AuthState
import com.brokentelephone.game.domain.user.BlockedUser
import com.brokentelephone.game.domain.user.OnboardingStep
import com.brokentelephone.game.domain.user.User
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.NetworkException
import com.brokentelephone.game.essentials.exceptions.auth.SamePasswordException
import com.brokentelephone.game.essentials.exceptions.auth.SessionDataException
import com.brokentelephone.game.essentials.exceptions.auth.TooManyRequestsException
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import com.brokentelephone.game.essentials.exceptions.auth.UnknownAuthException
import com.brokentelephone.game.essentials.exceptions.auth.WeakPasswordException
import com.google.firebase.messaging.FirebaseMessaging
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.RealtimeChannel
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.decodeRecord
import io.github.jan.supabase.realtime.postgresChangeFlow
import io.github.jan.supabase.realtime.realtime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class UserSessionImpl(
    private val supabase: SupabaseClient,
    private val dataStore: DataStore<Preferences>,
) : UserSession {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    override val user: Flow<User?> = _user.asStateFlow()

    private var realtimeChannel: RealtimeChannel? = null
    private var realtimeCollectJob: kotlinx.coroutines.Job? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun initialize() {
        Log.d("LOG_TAG", "initialize()")

        val status = supabase.auth.sessionStatus
            .first { it is SessionStatus.Authenticated || it is SessionStatus.NotAuthenticated }

        Log.d("LOG_TAG", "sessionStatus: $status")

        scope.launch {
            Log.d("LOG_TAG", "Start observe sessionStatus")
            supabase.auth.sessionStatus
                .filterNot { it is SessionStatus.Initializing }
                .flatMapLatest {
                    flowOf(supabase.auth.currentUserOrNull())
                }
                .distinctUntilChangedBy { it?.id }
                .collect { newSessionStatus ->
                    Log.d("LOG_TAG", "newSessionStatus: $newSessionStatus")

                    val currentUserId =
                        supabase.auth.currentUserOrNull() ?: run {
                            _authState.update { AuthState.NotAuth }
                            _user.update { null }

                            return@collect
                        }

                    observeSupabaseUser(currentUserId)
                }
        }
    }

    // TODO: Need review
    private suspend fun observeSupabaseUser(userInfo: UserInfo) {
        Log.d("LOG_TAG", "observeSupabaseUser(): $userInfo")

        _authState.update { AuthState.PreAuth(userInfo.id) }
        _user.update { userInfo.toUser() }

        realtimeCollectJob?.cancel()
        realtimeChannel?.let { channel ->
            supabase.realtime.removeChannel(channel)
        }

        val channel = supabase.realtime.channel("user-${userInfo.id}-${System.currentTimeMillis()}")
        realtimeChannel = channel

        val updateFlow = channel.postgresChangeFlow<PostgresAction.Update>(schema = "public") {
            table = COLLECTION_USERS
            filter(
                FilterOperation(
                    column = "id",
                    operator = FilterOperator.EQ,
                    value = userInfo.id
                )
            )
        }

        realtimeCollectJob = scope.launch {

            try {
                // First load
                val user = supabase.from(COLLECTION_USERS)
                    .select { filter { eq("id", userInfo.id) } }
                    .decodeSingleOrNull<UserDto>()
                    ?.toUser()
                    ?: throw SessionDataException()

                val updatedUser = user.copy(
                    email = userInfo.email ?: "",
                    isEmailVerified = userInfo.emailConfirmedAt != null
                )
                _authState.update { AuthState.Auth(updatedUser) }
                _user.update { updatedUser }
            } catch (_: Exception) {
                _authState.update { AuthState.NotAuth }
                _user.update { null }
            }

            updateFlow.collect { change ->
                Log.d("LOG_TAG", "Change: $change")
                try {
                    val newUser = change.decodeRecord<UserDto>().toUser()
                    val updatedNewUser = newUser.copy(
                        email = userInfo.email ?: "",
                        isEmailVerified = userInfo.emailConfirmedAt != null
                    )
                    _authState.update { AuthState.Auth(updatedNewUser) }
                    _user.update { updatedNewUser }
                } catch (e: Exception) {
                    Log.d("LOG_TAG", "updateFlow.collect { change -> $e")
                }
            }
        }

        channel.subscribe()
    }

    override fun getUserOnAuthStateChange(): Flow<User?> {
        return _authState
            .filter { it is AuthState.Auth || it is AuthState.NotAuth }
            .map { it.getUserOrNull() }
    }

    override suspend fun getUserId(): String? {
        return _authState.first { it !is AuthState.Loading }.getUserIdOrNull()
    }

    override suspend fun reloadUser() {
        val userInfo = supabase.auth.currentUserOrNull() ?: return
        observeSupabaseUser(userInfo)
    }

    override suspend fun updateUsername(username: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("username", username)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateBio(bio: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("bio", bio)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateAvatar(avatarUrl: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("avatar_url", avatarUrl)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeAvatarStep(avatarUrl: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("avatar_url", avatarUrl)
                    put("onboarding_step", OnboardingStep.CHOOSE_USERNAME.name)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun completeUsernameStep(username: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("username", username)
                    put("onboarding_step", OnboardingStep.COMPLETED.name)
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun markPostAsNotInterested(postId: String) {
        dataStore.edit { prefs ->
            val current = prefs[KEY_NOT_INTERESTED_POSTS] ?: emptySet()
            prefs[KEY_NOT_INTERESTED_POSTS] = current + postId
        }
    }

    // TODO: Migration to Supabase
    override suspend fun getNotInterestedPostIds(): List<String> {
        return dataStore.data.firstOrNull()?.get(KEY_NOT_INTERESTED_POSTS)?.toList() ?: emptyList()
    }

    override suspend fun blockUser(userId: String) {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("user_blocks").insert(
                buildJsonObject {
                    put("blocker_id", currentUserId)
                    put("blocked_id", userId)
                    put("created_at", System.currentTimeMillis())
                }
            )
        } catch (e: RestException) {
            Log.d("LOG_TAG", "blockUser(): $e")
            throw UnknownAuthException()
        } catch (e: java.io.IOException) {
            Log.d("LOG_TAG", "blockUser(): $e")
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "blockUser(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun unblockUser(userId: String) {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from("user_blocks").delete {
                filter {
                    eq("blocker_id", currentUserId)
                    eq("blocked_id", userId)
                }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun updateNotifications(notifications: List<NotificationType>) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put(
                        "notifications",
                        buildJsonArray { notifications.forEach { add(JsonPrimitive(it.name)) } })
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (e: RestException) {
            Log.d("LOG_TAG", "updateNotifications: $e")
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "updateNotifications: $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun updatePermissions(permissions: UserPermissions) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject {
                    put("permissions", buildJsonObject {
                        put("isNotificationsGranted", permissions.isNotificationsGranted)
                    })
                    put("updated_at", System.currentTimeMillis())
                }
            ) {
                filter { eq("id", userId) }
            }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun deleteFcmToken() {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        Log.d("LOG_TAG", "deleteFcmToken")
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject { put("fcm_token", JsonNull) }
            ) {
                filter { eq("id", userId) }
            }
            FirebaseMessaging.getInstance().deleteToken().await()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "deleteFcmToken: $e")
            e.printStackTrace()
        }
    }

    override fun refreshFcmToken() {
        scope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                Log.d("LOG_TAG", "refreshFcmToken: $token")
                updateFcmToken(token)
            } catch (e: Exception) {
                Log.d("LOG_TAG", "refreshFcmToken: $e")
                e.printStackTrace()
            }
        }
    }

    override suspend fun updateFcmToken(token: String) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        Log.d("LOG_TAG", "updateFcmToken: $token")
        try {
            supabase.from(COLLECTION_USERS).update(
                buildJsonObject { put("fcm_token", token) }
            ) {
                filter { eq("id", userId) }
            }
        } catch (e: Exception) {
            Log.d("LOG_TAG", "updateFcmToken: $e")
            e.printStackTrace()
        }
    }

    override suspend fun updatePassword(newPassword: String) {
        supabase.auth.currentUserOrNull() ?: throw UnauthorizedException()
        try {
            supabase.auth.updateUser {
                password = newPassword
            }
        } catch (e: RestException) {
            Log.d("LOG_TAG", "E: $e")
            val msg = e.message.orEmpty()
            when {
                msg.contains("same_password", ignoreCase = true) ||
                        msg.contains(
                            "different from",
                            ignoreCase = true
                        ) -> throw SamePasswordException()

                msg.contains("weak_password", ignoreCase = true) ||
                        msg.contains("at least", ignoreCase = true) -> throw WeakPasswordException()

                msg.contains("rate_limit", ignoreCase = true) -> throw TooManyRequestsException()
                else -> throw UnknownAuthException()
            }
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun signOut() {
        dataStore.edit { it.remove(KEY_NOT_INTERESTED_POSTS) }
        realtimeChannel?.let { supabase.realtime.removeChannel(it) }
        realtimeChannel = null
        supabase.auth.signOut()
        _authState.update { AuthState.NotAuth }
        _user.update { null }
    }

    override suspend fun deleteAccount() = Unit

    override suspend fun getExcludedUserIds(): List<String> {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        return try {
            supabase.from(COLLECTION_USER_BLOCKS)
                .select {
                    filter {
                        or {
                            eq("blocker_id", currentUserId)
                            eq("blocked_id", currentUserId)
                        }
                    }
                }
                .decodeList<UserBlockDto>()
                .map { block ->
                    if (block.blockerId == currentUserId) block.blockedId else block.blockerId
                }
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getExcludedUserIds(): $e")
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getExcludedUserIds(): $e")
            throw UnknownAuthException()
        }
    }

    override suspend fun getBlockedUsers(): List<BlockedUser> {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        return try {
            supabase.from(COLLECTION_USER_BLOCKS)
                .select(Columns.raw("*, users!blocked_id(username, avatar_url)")) {
                    filter { eq("blocker_id", currentUserId) }
                }
                .decodeList<UserBlockWithUserDto>()
                .map { it.toBlockedUser() }
        } catch (_: RestException) {
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (_: Exception) {
            throw UnknownAuthException()
        }
    }

    override suspend fun getBlockedUsersCount(): Int {
        val currentUserId = supabase.auth.currentUserOrNull()?.id ?: throw UnauthorizedException()
        return try {
            supabase.from(COLLECTION_USER_BLOCKS)
                .select {
                    filter { eq("blocker_id", currentUserId) }
                    count(Count.EXACT)
                }
                .countOrNull()?.toInt() ?: 0
        } catch (e: RestException) {
            Log.d("LOG_TAG", "getBlockedUsersCount(): $e")
            throw UnknownAuthException()
        } catch (_: java.io.IOException) {
            throw NetworkException()
        } catch (e: Exception) {
            Log.d("LOG_TAG", "getBlockedUsersCount(): $e")
            throw UnknownAuthException()
        }
    }

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_USER_BLOCKS = "user_blocks"
        private val KEY_NOT_INTERESTED_POSTS = stringSetPreferencesKey("not_interested_posts")
    }
}
