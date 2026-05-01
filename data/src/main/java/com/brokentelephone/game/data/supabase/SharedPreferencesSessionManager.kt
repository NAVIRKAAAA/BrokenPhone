package com.brokentelephone.game.data.supabase

import android.content.Context
import androidx.core.content.edit
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.json.Json

class SharedPreferencesSessionManager(context: Context) : SessionManager {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun loadSession(): UserSession? {
        val sessionJson = prefs.getString(KEY_SESSION, null) ?: return null
        return runCatching { json.decodeFromString<UserSession>(sessionJson) }
            .getOrNull()
    }

    override suspend fun saveSession(session: UserSession) {
        prefs.edit { putString(KEY_SESSION, json.encodeToString(session)) }
    }

    override suspend fun deleteSession() {
        prefs.edit { remove(KEY_SESSION) }
    }

    companion object {
        private const val PREFS_NAME = "supabase_session"
        private const val KEY_SESSION = "session"
    }
}
