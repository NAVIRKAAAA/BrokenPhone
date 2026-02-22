package com.broken.telephone.domain.repository

import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.domain.settings.Language
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    fun getLanguage(): Flow<Language>
    fun getTheme(): Flow<AppTheme>
}
