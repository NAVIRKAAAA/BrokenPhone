package com.brokentelephone.game.domain.use_case

import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import kotlinx.coroutines.flow.Flow

class GetLanguageUseCase(
    private val repository: UserSettingsRepository,
) {
    operator fun invoke(): Flow<Language> = repository.getLanguage()
}
