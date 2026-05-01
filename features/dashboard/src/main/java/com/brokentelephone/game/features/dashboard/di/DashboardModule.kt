package com.brokentelephone.game.features.dashboard.di

import com.brokentelephone.game.dashboard_api.DashboardNavigationApi
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.features.dashboard.DashboardViewModel
import com.brokentelephone.game.features.dashboard.api.DashboardNavigationApiImpl
import com.brokentelephone.game.features.dashboard.use_case.LoadInitialPostsUseCase
import com.brokentelephone.game.features.dashboard.use_case.LoadNextPostsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dashboardModule = module {
    factoryOf(::LoadInitialPostsUseCase)
    factoryOf(::LoadNextPostsUseCase)
    factoryOf(::DeletePostUseCase)
    viewModelOf(::DashboardViewModel)
    single<DashboardNavigationApi> { DashboardNavigationApiImpl() }
}
