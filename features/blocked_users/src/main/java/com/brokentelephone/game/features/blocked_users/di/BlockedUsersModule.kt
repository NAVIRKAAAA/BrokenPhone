package com.brokentelephone.game.features.blocked_users.di

import com.brokentelephone.game.blocked_users_api.BlockedUsersNavigationApi
import com.brokentelephone.game.features.blocked_users.BlockedUsersViewModel
import com.brokentelephone.game.features.blocked_users.api.BlockedUsersNavigationApiImpl
import com.brokentelephone.game.features.blocked_users.use_case.GetBlockedUsersUseCase
import com.brokentelephone.game.features.blocked_users.use_case.UnblockUserUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val blockedUsersModule = module {
    factoryOf(::GetBlockedUsersUseCase)
    factoryOf(::UnblockUserUseCase)
    viewModelOf(::BlockedUsersViewModel)
    single<BlockedUsersNavigationApi> { BlockedUsersNavigationApiImpl() }
}
