package com.brokentelephone.game.features.edit_email.di

import com.brokentelephone.game.features.edit_email.EditEmailViewModel
import com.brokentelephone.game.features.edit_email.use_case.SendEmailChangeVerificationUseCase
import com.brokentelephone.game.features.edit_email.use_case.ValidateEmailUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editEmailModule = module {
    viewModelOf(::EditEmailViewModel)
    factoryOf(::ValidateEmailUseCase)
    factoryOf(::SendEmailChangeVerificationUseCase)
}
