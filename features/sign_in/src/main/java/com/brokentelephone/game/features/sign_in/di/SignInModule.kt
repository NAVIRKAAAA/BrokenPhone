package com.brokentelephone.game.features.sign_in.di

import com.brokentelephone.game.features.sign_in.SignInViewModel
import com.brokentelephone.game.features.sign_in.api.SignInNavigationApiImpl
import com.brokentelephone.game.features.sign_in.use_case.SignInWithEmailPasswordUseCase
import com.brokentelephone.game.sign_in_api.SignInNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signInModule = module {
    single<SignInNavigationApi> {
        SignInNavigationApiImpl()
    }
    factoryOf(::SignInWithEmailPasswordUseCase)
    viewModelOf(::SignInViewModel)
}
