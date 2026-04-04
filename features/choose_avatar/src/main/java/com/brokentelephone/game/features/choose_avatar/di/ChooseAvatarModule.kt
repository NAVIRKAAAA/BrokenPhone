package com.brokentelephone.game.features.choose_avatar.di

import com.brokentelephone.game.choose_avatar_api.ChooseAvatarNavigationApi
import com.brokentelephone.game.features.choose_avatar.ChooseAvatarViewModel
import com.brokentelephone.game.features.choose_avatar.api.ChooseAvatarNavigationApiImpl
import com.brokentelephone.game.features.choose_avatar.use_case.CompleteAvatarStepUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chooseAvatarModule = module {
    factoryOf(::CompleteAvatarStepUseCase)
    viewModelOf(::ChooseAvatarViewModel)
    single<ChooseAvatarNavigationApi> { ChooseAvatarNavigationApiImpl(get()) }
}
