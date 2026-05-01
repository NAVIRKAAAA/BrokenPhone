package com.brokentelephone.game.features.edit_username.di

import com.brokentelephone.game.edit_username_api.EditUsernameNavigationApi
import com.brokentelephone.game.features.edit_username.EditUsernameViewModel
import com.brokentelephone.game.features.edit_username.api.EditUsernameNavigationApiImpl
import com.brokentelephone.game.features.edit_username.use_case.UpdateUsernameUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editUsernameModule = module {
    factoryOf(::UpdateUsernameUseCase)
    viewModelOf(::EditUsernameViewModel)
    single<EditUsernameNavigationApi> { EditUsernameNavigationApiImpl() }
}
