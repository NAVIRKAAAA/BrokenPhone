package com.brokentelephone.game.features.app_preferences.use_case

import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.settings.Language
import kotlinx.coroutines.flow.Flow

class GetLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<Language> = repository.getLanguage()
}
