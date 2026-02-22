package com.broken.telephone.data.repository

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.domain.settings.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockUserSettingsRepositoryImpl : UserSettingsRepository {
    override fun getLanguage(): Flow<Language> = flowOf(Language.ENGLISH)
    override fun getTheme(): Flow<AppTheme> = flowOf(AppTheme.LIGHT)
}
