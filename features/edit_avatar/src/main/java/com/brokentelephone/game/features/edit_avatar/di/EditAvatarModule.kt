package com.brokentelephone.game.features.edit_avatar.di

import com.brokentelephone.game.edit_avatar_api.EditAvatarNavigationApi
import com.brokentelephone.game.features.edit_avatar.EditAvatarViewModel
import com.brokentelephone.game.features.edit_avatar.api.EditAvatarNavigationApiImpl
import com.brokentelephone.game.features.edit_avatar.use_case.UpdateAvatarUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editAvatarModule = module {
    factoryOf(::UpdateAvatarUseCase)
    viewModelOf(::EditAvatarViewModel)

    single<EditAvatarNavigationApi> { EditAvatarNavigationApiImpl() }
}
