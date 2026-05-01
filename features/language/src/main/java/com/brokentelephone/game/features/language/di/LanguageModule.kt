package com.brokentelephone.game.features.language.di

import com.brokentelephone.game.features.language.LanguageViewModel
import com.brokentelephone.game.features.language.api.LanguageNavigationApiImpl
import com.brokentelephone.game.features.language.use_case.InitializeFirstAppLaunchUseCase
import com.brokentelephone.game.features.language.use_case.UpdateLanguageUseCase
import com.brokentelephone.game.language_api.LanguageNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val languageModule = module {
    factoryOf(::UpdateLanguageUseCase)
    factoryOf(::InitializeFirstAppLaunchUseCase)
    viewModelOf(::LanguageViewModel)
    single<LanguageNavigationApi> { LanguageNavigationApiImpl() }
}
