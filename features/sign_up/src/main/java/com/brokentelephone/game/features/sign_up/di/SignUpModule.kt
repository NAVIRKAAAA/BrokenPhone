package com.brokentelephone.game.features.sign_up.di

import com.brokentelephone.game.essentials.validation.SignUpValidator
import com.brokentelephone.game.features.sign_up.SignUpViewModel
import com.brokentelephone.game.features.sign_up.use_case.SignUpUseCase
import com.brokentelephone.game.features.sign_up.use_case.ValidateSignUpUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val signUpModule = module {
    factoryOf(::SignUpValidator)
    factoryOf(::ValidateSignUpUseCase)
    factoryOf(::SignUpUseCase)
    viewModelOf(::SignUpViewModel)
}
