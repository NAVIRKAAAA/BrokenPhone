package com.brokentelephone.game.features.language.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.exceptions.auth.UnauthorizedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull

class UpdateLanguageUseCase(
    private val repository: UserSettingsRepository,
    private val userSession: UserSession,
    private val handler: ApiHandler
) {
    suspend operator fun invoke(language: Language) {
        repository.updateLanguage(language)

        handler.handle(Dispatchers.IO) {
            userSession.getUser().firstOrNull() ?: throw UnauthorizedException()

//            userSession.updateLanguage(language)
        }
    }
}
