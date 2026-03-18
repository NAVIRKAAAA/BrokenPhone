package com.brokentelephone.game.features.app_preferences.use_case

import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow

class GetThemeUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<AppTheme> = repository.getTheme()
}
