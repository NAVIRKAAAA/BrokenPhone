package com.brokentelephone.game.features.language.use_case

import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.model.settings.Language
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.user.UserSession
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
            val isAuth = userSession.authState.firstOrNull()?.isAuth() ?: false

            if (isAuth) {
                userSession.updateLanguage(language)
            }
        }
    }
}
