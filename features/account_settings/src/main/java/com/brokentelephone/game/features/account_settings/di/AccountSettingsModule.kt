package com.brokentelephone.game.features.account_settings.di

import com.brokentelephone.game.account_settings_api.AccountSettingsNavigationApi
import com.brokentelephone.game.features.account_settings.AccountSettingsViewModel
import com.brokentelephone.game.features.account_settings.api.AccountSettingsNavigationApiImpl
import com.brokentelephone.game.features.account_settings.use_case.DeleteAccountUseCase
import com.brokentelephone.game.features.account_settings.use_case.SendEmailVerificationUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val accountSettingsModule = module {
    factoryOf(::DeleteAccountUseCase)
    factoryOf(::SendEmailVerificationUseCase)
    viewModelOf(::AccountSettingsViewModel)
    single<AccountSettingsNavigationApi> { AccountSettingsNavigationApiImpl() }
}
