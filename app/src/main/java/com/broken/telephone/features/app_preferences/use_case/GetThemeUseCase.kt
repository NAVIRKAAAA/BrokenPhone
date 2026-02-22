package com.broken.telephone.features.app_preferences.use_case

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.AppTheme
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<AppTheme> = repository.getTheme()
}
