package com.brokentelephone.game.features.friends.di

import com.brokentelephone.game.domain.use_case.GetFriendsUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.features.friends.FriendsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val friendsModule = module {
    factoryOf(::GetFriendsUseCase)
    factoryOf(::RemoveFriendUseCase)
    viewModelOf(::FriendsViewModel)
}
