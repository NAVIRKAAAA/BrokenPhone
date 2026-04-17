package com.brokentelephone.game.features.edit_email.di

import com.brokentelephone.game.domain.use_case.SetPendingEmailUseCase
import com.brokentelephone.game.edit_email_api.EditEmailNavigationApi
import com.brokentelephone.game.features.edit_email.EditEmailViewModel
import com.brokentelephone.game.features.edit_email.api.EditEmailNavigationApiImpl
import com.brokentelephone.game.features.edit_email.use_case.SendEmailChangeVerificationUseCase
import com.brokentelephone.game.features.edit_email.use_case.ValidateEmailUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editEmailModule = module {
    viewModelOf(::EditEmailViewModel)
    factoryOf(::ValidateEmailUseCase)
    factoryOf(::SendEmailChangeVerificationUseCase)
    factoryOf(::SetPendingEmailUseCase)

    single<EditEmailNavigationApi> { EditEmailNavigationApiImpl() }
}
