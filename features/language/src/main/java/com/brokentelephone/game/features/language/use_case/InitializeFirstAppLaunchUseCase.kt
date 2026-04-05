package com.brokentelephone.game.features.language.use_case

import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import java.util.Locale

class InitializeFirstAppLaunchUseCase(
    private val repository: UserSettingsRepository,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
) {
    suspend fun execute() {
        if (!repository.isFirstLaunch()) return

        val deviceLanguageTag = Locale.getDefault().language

        val language =
            if (deviceLanguageTag.startsWith("uk")) Language.UKRAINIAN else Language.ENGLISH
        updateLanguageUseCase(language)
        repository.markFirstLaunchComplete()
    }
}
