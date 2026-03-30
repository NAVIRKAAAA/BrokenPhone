package com.brokentelephone.game.features.user_details.di

import com.brokentelephone.game.domain.use_case.AcceptFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.CancelFriendRequestUseCase
import com.brokentelephone.game.domain.use_case.GetUserLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.RemoveFriendUseCase
import com.brokentelephone.game.domain.use_case.SendFriendRequestUseCase
import com.brokentelephone.game.features.user_details.UserDetailsViewModel
import com.brokentelephone.game.features.user_details.use_case.GetFriendshipActionStateUseCase
import com.brokentelephone.game.features.user_details.use_case.GetUserByIdUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userDetailsModule = module {
    factoryOf(::GetUserByIdUseCase)
    factoryOf(::GetFriendshipActionStateUseCase)
    factoryOf(::SendFriendRequestUseCase)
    factoryOf(::GetUserLinkByIdUseCase)
    factoryOf(::AcceptFriendRequestUseCase)
    factoryOf(::RemoveFriendUseCase)
    factoryOf(::CancelFriendRequestUseCase)
    viewModelOf(::UserDetailsViewModel)
}
