package com.broken.telephone.main

import com.broken.telephone.domain.settings.AppTheme

data class MainState(
    val theme: AppTheme = AppTheme.SYSTEM,
)
