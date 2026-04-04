package com.brokentelephone.game.features.forgot_password.di

import com.brokentelephone.game.features.forgot_password.ForgotPasswordViewModel
import com.brokentelephone.game.features.forgot_password.api.ForgotPasswordNavigationApiImpl
import com.brokentelephone.game.features.forgot_password.use_case.SendPasswordResetEmailUseCase
import com.brokentelephone.game.forgot_password_api.ForgotPasswordNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val forgotPasswordModule = module {
    factoryOf(::SendPasswordResetEmailUseCase)
    viewModelOf(::ForgotPasswordViewModel)
    single<ForgotPasswordNavigationApi> { ForgotPasswordNavigationApiImpl() }
}
