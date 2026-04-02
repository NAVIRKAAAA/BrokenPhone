package com.brokentelephone.game.features.notifications.di

import com.brokentelephone.game.features.notifications.NotificationsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsModule = module {
    viewModelOf(::NotificationsViewModel)
}
