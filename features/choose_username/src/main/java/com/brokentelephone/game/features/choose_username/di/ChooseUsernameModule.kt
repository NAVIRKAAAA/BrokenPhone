package com.brokentelephone.game.features.choose_username.di

import com.brokentelephone.game.choose_username_api.ChooseUsernameNavigationApi
import com.brokentelephone.game.features.choose_username.ChooseUsernameViewModel
import com.brokentelephone.game.features.choose_username.api.ChooseUsernameNavigationApiImpl
import com.brokentelephone.game.features.choose_username.use_case.CompleteUsernameStepUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chooseUsernameModule = module {
    factoryOf(::CompleteUsernameStepUseCase)
    viewModelOf(::ChooseUsernameViewModel)
    single<ChooseUsernameNavigationApi> { ChooseUsernameNavigationApiImpl(get()) }

}
