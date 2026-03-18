package com.brokentelephone.game.features.language.use_case

import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository

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
