package com.brokentelephone.game.features.profile.di

import com.brokentelephone.game.domain.use_case.GetFriendsCountUseCase
import com.brokentelephone.game.features.profile.ProfileViewModel
import com.brokentelephone.game.features.profile.api.ProfileNavigationApiImpl
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    factoryOf(::GetFriendsCountUseCase)
    viewModelOf(::ProfileViewModel)

    single<ProfileNavigationApi> {
        ProfileNavigationApiImpl()
    }
}
