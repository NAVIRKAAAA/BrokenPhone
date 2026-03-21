package com.brokentelephone.game.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class UserSettingsRepositoryImpl(
    private val context: Context,
) : UserSettingsRepository {

    override fun getLanguage(): Flow<Language> {
        return context.dataStore.data.map { prefs ->
            val value = prefs[KEY_LANGUAGE] ?: Language.ENGLISH.name
            Language.entries.firstOrNull { it.name == value } ?: Language.ENGLISH
        }
    }

    override fun getTheme(): Flow<AppTheme> {
        return context.dataStore.data.map { prefs ->
            val value = prefs[KEY_THEME] ?: AppTheme.SYSTEM.name
            AppTheme.entries.firstOrNull { it.name == value } ?: AppTheme.SYSTEM
        }
    }

    override fun getPendingEmail(): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            val value = prefs[KEY_PENDING_EMAIL]
            value
        }
    }

    override suspend fun isFirstLaunch(): Boolean {
        return context.dataStore.data.first()[KEY_FIRST_LAUNCH] ?: true
    }

    override suspend fun markFirstLaunchComplete() {
        context.dataStore.edit { prefs -> prefs[KEY_FIRST_LAUNCH] = false }
    }

    override suspend fun updateLanguage(language: Language) {
        context.dataStore.edit { prefs -> prefs[KEY_LANGUAGE] = language.name }
    }

    override suspend fun updateTheme(theme: AppTheme) {
        context.dataStore.edit { prefs -> prefs[KEY_THEME] = theme.name }
    }

    override suspend fun setPendingEmail(email: String) {
        context.dataStore.edit { prefs -> prefs[KEY_PENDING_EMAIL] = email }
    }

    companion object {
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_THEME = stringPreferencesKey("theme")
        private val KEY_FIRST_LAUNCH = booleanPreferencesKey("first_launch")
        private val KEY_PENDING_EMAIL = stringPreferencesKey("pending_email")
    }
}
