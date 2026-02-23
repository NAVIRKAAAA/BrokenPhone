package com.broken.telephone.features.theme.use_case

import com.broken.telephone.domain.repository.UserSettingsRepository
import com.broken.telephone.domain.settings.AppTheme

class UpdateThemeUseCase(
    private val repository: UserSettingsRepository,
) {
    suspend operator fun invoke(theme: AppTheme) = repository.updateTheme(theme)
}
