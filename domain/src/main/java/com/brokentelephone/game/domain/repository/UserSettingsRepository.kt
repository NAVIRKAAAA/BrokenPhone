package com.brokentelephone.game.domain.repository

import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.model.settings.Language
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getLanguage(): Flow<Language>
    fun getTheme(): Flow<AppTheme>
    suspend fun isFirstLaunch(): Boolean
    suspend fun markFirstLaunchComplete()
    suspend fun updateLanguage(language: Language)
    suspend fun updateTheme(theme: AppTheme)
}
