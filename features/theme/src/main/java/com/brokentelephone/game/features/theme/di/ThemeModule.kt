package com.brokentelephone.game.features.theme.di

import com.brokentelephone.game.features.theme.ThemeViewModel
import com.brokentelephone.game.features.theme.api.ThemeNavigationApiImpl
import com.brokentelephone.game.features.theme.use_case.UpdateThemeUseCase
import com.brokentelephone.game.theme_api.ThemeNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val themeModule = module {
    factoryOf(::UpdateThemeUseCase)
    viewModelOf(::ThemeViewModel)
    single<ThemeNavigationApi> { ThemeNavigationApiImpl() }
}
