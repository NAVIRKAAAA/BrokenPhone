package com.brokentelephone.game.features.settings.di

import com.brokentelephone.game.domain.use_case.GetAuthStateUseCase
import com.brokentelephone.game.features.settings.SettingsViewModel
import com.brokentelephone.game.features.settings.api.SettingsNavigationApiImpl
import com.brokentelephone.game.features.settings.use_case.GetBlockedUsersCountUseCase
import com.brokentelephone.game.features.settings.use_case.GetVersionInfoUseCase
import com.brokentelephone.game.settings_api.SettingsNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    factoryOf(::GetVersionInfoUseCase)
    factoryOf(::GetBlockedUsersCountUseCase)
    factoryOf(::GetAuthStateUseCase)
    viewModelOf(::SettingsViewModel)
    single<SettingsNavigationApi> { SettingsNavigationApiImpl() }
}
