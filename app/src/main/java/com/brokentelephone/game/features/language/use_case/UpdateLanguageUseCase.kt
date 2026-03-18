package com.brokentelephone.game.features.language.use_case

import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository

class UpdateLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(language: Language) = repository.updateLanguage(language)
}
