package com.brokentelephone.game.features.profile.di

import com.brokentelephone.game.features.profile.ProfileViewModel
import com.brokentelephone.game.features.profile.api.ProfileNavigationApiImpl
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    viewModelOf(::ProfileViewModel)

    single<ProfileNavigationApi> {
        ProfileNavigationApiImpl()
    }
}
