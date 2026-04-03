package com.brokentelephone.game.features.notifications.di

import com.brokentelephone.game.features.notifications.NotificationsViewModel
import com.brokentelephone.game.features.notifications.use_case.GetNotificationsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsModule = module {
    viewModelOf(::NotificationsViewModel)

    factoryOf(::GetNotificationsUseCase)
}
