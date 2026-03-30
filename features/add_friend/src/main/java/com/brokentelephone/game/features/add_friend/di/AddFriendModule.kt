package com.brokentelephone.game.features.add_friend.di

import com.brokentelephone.game.domain.use_case.AcceptFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.CancelFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.DeclineFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.domain.use_case.SendFriendRequestUseCase
import com.brokentelephone.game.features.add_friend.AddFriendViewModel
import com.brokentelephone.game.features.add_friend.use_case.GetPendingInvitesUseCase
import com.brokentelephone.game.features.add_friend.use_case.GetReceivedPendingInvitesUseCase
import com.brokentelephone.game.features.add_friend.use_case.SearchUsersUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val addFriendModule = module {
    factoryOf(::SearchUsersUseCase)
    factoryOf(::SendFriendRequestUseCase)
    factoryOf(::GetPendingInvitesUseCase)
    factoryOf(::GetReceivedPendingInvitesUseCase)
    factoryOf(::AcceptFriendRequestUseCase)
    factoryOf(::DeclineFriendRequestUseCase)
    factoryOf(::CancelFriendRequestUseCase)
    factoryOf(::RemoveFriendUseCase)
    viewModelOf(::AddFriendViewModel)
}
