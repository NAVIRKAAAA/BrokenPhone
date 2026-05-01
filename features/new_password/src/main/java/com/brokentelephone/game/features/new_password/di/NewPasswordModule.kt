package com.brokentelephone.game.features.new_password.di

import com.brokentelephone.game.domain.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.essentials.validation.SignUpValidator
import com.brokentelephone.game.features.new_password.NewPasswordViewModel
import com.brokentelephone.game.features.new_password.api.NewPasswordNavigationApiImpl
import com.brokentelephone.game.features.new_password.use_case.UpdatePasswordUseCase
import com.brokentelephone.game.features.new_password.use_case.ValidateNewPasswordUseCase
import com.brokentelephone.game.new_password_api.NewPasswordNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val newPasswordModule = module {
    single<NewPasswordNavigationApi> { NewPasswordNavigationApiImpl() }
    factoryOf(::GetPendingEmailUseCase)
    factoryOf(::SignUpValidator)
    factoryOf(::ValidateNewPasswordUseCase)
    factoryOf(::UpdatePasswordUseCase)
    viewModelOf(::NewPasswordViewModel)
}
