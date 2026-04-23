package com.brokentelephone.game.features.chain_details.di

import com.brokentelephone.game.chain_details_api.ChainDetailsNavigationApi
import com.brokentelephone.game.features.chain_details.ChainDetailsViewModel
import com.brokentelephone.game.features.chain_details.api.ChainDetailsNavigationApiImpl
import com.brokentelephone.game.features.chain_details.use_case.GetChainByIdUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chainDetailsModule = module {
    factoryOf(::GetChainByIdUseCase)
    viewModelOf(::ChainDetailsViewModel)
    single<ChainDetailsNavigationApi> { ChainDetailsNavigationApiImpl() }
}
