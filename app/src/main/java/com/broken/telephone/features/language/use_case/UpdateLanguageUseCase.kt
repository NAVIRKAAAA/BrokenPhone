package com.broken.telephone.features.language.use_case

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.Language

class UpdateLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(language: Language) = repository.updateLanguage(language)
}
