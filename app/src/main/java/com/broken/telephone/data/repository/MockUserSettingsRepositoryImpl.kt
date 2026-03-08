package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.domain.settings.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockUserSettingsRepositoryImpl : UserSettingsRepository {

    private val _language = MutableStateFlow(Language.ENGLISH)
    private val _theme = MutableStateFlow(AppTheme.SYSTEM)
    private var isFirstLaunch = true

    override fun getLanguage(): Flow<Language> = _language.asStateFlow()
    override fun getTheme(): Flow<AppTheme> = _theme.asStateFlow()

    override suspend fun isFirstLaunch(): Boolean = isFirstLaunch

    override suspend fun markFirstLaunchComplete() {
        isFirstLaunch = false
    }

    override suspend fun updateLanguage(language: Language) {
        _language.value = language
    }

    override suspend fun updateTheme(theme: AppTheme) {
        _theme.value = theme
    }
}
