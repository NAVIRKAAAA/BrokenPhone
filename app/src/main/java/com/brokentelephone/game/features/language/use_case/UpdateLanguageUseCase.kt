package com.brokentelephone.game.features.language.use_case

import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.settings.Language

class UpdateLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(language: Language) = repository.updateLanguage(language)
}
