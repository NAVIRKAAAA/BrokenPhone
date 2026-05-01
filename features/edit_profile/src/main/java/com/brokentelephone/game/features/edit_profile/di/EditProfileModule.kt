package com.brokentelephone.game.features.edit_profile.di

import com.brokentelephone.game.edit_profile_api.EditProfileNavigationApi
import com.brokentelephone.game.features.edit_profile.EditProfileViewModel
import com.brokentelephone.game.features.edit_profile.api.EditProfileNavigationApiImpl
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editProfileModule = module {
    viewModelOf(::EditProfileViewModel)

    single<EditProfileNavigationApi> { EditProfileNavigationApiImpl() }

}
