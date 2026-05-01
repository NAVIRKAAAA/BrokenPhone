package com.brokentelephone.game.features.user_friends.di

import com.brokentelephone.game.domain.use_case.GetUserByIdUseCase
import com.brokentelephone.game.features.user_friends.UserFriendsViewModel
import com.brokentelephone.game.features.user_friends.api.UserFriendsNavigationApiImpl
import com.brokentelephone.game.user_friends_api.UserFriendsNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userFriendsModule = module {
    viewModelOf(::UserFriendsViewModel)
    factoryOf(::GetUserByIdUseCase)
    single<UserFriendsNavigationApi> { UserFriendsNavigationApiImpl() }
}
