package com.brokentelephone.game.features.user_details.di

import com.brokentelephone.game.features.user_details.UserDetailsViewModel
import com.brokentelephone.game.features.user_details.use_case.GetUserByIdUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userDetailsModule = module {
    factoryOf(::GetUserByIdUseCase)
    viewModelOf(::UserDetailsViewModel)
}
