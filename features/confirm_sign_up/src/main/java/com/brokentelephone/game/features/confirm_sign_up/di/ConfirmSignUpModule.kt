package com.brokentelephone.game.features.confirm_sign_up.di

import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.features.confirm_sign_up.ConfirmSignUpViewModel
import com.brokentelephone.game.features.confirm_sign_up.api.ConfirmSignUpNavigationApiImpl
import com.brokentelephone.game.features.confirm_sign_up.use_case.ResendSignUpConfirmationUseCase
import com.brokentelephone.game.features.confirm_sign_up.use_case.VerifyEmailOtpUseCase
import com.brokentelephone.game.features.confirm_sign_up_api.ConfirmSignUpNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val confirmSignUpModule = module {
    single<ConfirmSignUpNavigationApi> { ConfirmSignUpNavigationApiImpl() }
    factory { CountdownTimer() }
    factoryOf(::ResendSignUpConfirmationUseCase)
    factoryOf(::VerifyEmailOtpUseCase)
    viewModel { (email: String) -> ConfirmSignUpViewModel(email = email, countdownTimer = get(), verifyEmailOtpUseCase = get(), resendSignUpConfirmationUseCase = get(), exceptionToMessageMapper = get()) }
}
