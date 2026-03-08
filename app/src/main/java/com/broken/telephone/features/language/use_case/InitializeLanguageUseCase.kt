package com.broken.telephone.features.language.use_case

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.Language

class InitializeLanguageUseCase(
    private val repository: UserSettingsRepository,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
) {
    suspend operator fun invoke(deviceLanguageTag: String) {
        if (!repository.isFirstLaunch()) return
        val language = if (deviceLanguageTag.startsWith("uk")) Language.UKRAINIAN else Language.ENGLISH
        updateLanguageUseCase(language)
        repository.markFirstLaunchComplete()
    }
}
