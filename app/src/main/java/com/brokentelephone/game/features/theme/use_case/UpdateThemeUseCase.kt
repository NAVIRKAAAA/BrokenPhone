package com.brokentelephone.game.features.theme.use_case

import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.settings.AppTheme

class UpdateThemeUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(theme: AppTheme) = repository.updateTheme(theme)
}
