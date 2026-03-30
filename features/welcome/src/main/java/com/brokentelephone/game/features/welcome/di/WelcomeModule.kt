package com.brokentelephone.game.features.welcome.di

import com.brokentelephone.game.features.welcome.WelcomeViewModel
import com.brokentelephone.game.features.welcome.use_case.SignInAnonymouslyUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val welcomeModule = module {
    factoryOf(::SignInAnonymouslyUseCase)
    viewModelOf(::WelcomeViewModel)
}
