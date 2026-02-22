package com.broken.telephone.features.app_preferences.use_case

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.Language
import kotlinx.coroutines.flow.Flow

class GetLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<Language> = repository.getLanguage()
}
