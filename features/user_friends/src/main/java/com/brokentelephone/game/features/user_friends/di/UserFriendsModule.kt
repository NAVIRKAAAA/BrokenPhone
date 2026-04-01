package com.brokentelephone.game.features.user_friends.di

import com.brokentelephone.game.domain.use_case.GetUserByIdUseCase
import com.brokentelephone.game.features.user_friends.UserFriendsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userFriendsModule = module {
    viewModelOf(::UserFriendsViewModel)
    factoryOf(::GetUserByIdUseCase)
}
