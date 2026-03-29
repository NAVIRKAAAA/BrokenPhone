package com.brokentelephone.game.features.add_friend.di

import com.brokentelephone.game.features.add_friend.AddFriendViewModel
import com.brokentelephone.game.features.add_friend.use_case.GetPendingInvitesUseCase
import com.brokentelephone.game.features.add_friend.use_case.SearchUsersUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val addFriendModule = module {
    factoryOf(::SearchUsersUseCase)
    factoryOf(::GetPendingInvitesUseCase)
    viewModelOf(::AddFriendViewModel)
}
