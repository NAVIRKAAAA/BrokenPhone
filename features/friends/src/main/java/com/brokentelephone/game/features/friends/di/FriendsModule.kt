package com.brokentelephone.game.features.friends.di

import com.brokentelephone.game.domain.use_case.AcceptFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.CancelFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.GetFriendsUseCase
import com.brokentelephone.game.domain.use_case.GetSuggestedUsersUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.domain.use_case.SendFriendRequestUseCase
import com.brokentelephone.game.features.friends.FriendsViewModel
import com.brokentelephone.game.features.friends.api.FriendsNavigationApiImpl
import com.brokentelephone.game.friends_api.FriendsNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val friendsModule = module {
    factoryOf(::GetFriendsUseCase)
    factoryOf(::GetSuggestedUsersUseCase)
    factoryOf(::RemoveFriendUseCase)
    factoryOf(::AcceptFriendRequestUseCase)
    factoryOf(::CancelFriendRequestUseCase)
    factoryOf(::SendFriendRequestUseCase)
    viewModelOf(::FriendsViewModel)
    single<FriendsNavigationApi> { FriendsNavigationApiImpl() }
}
