package com.brokentelephone.game.features.notifications_settings.di

import com.brokentelephone.game.features.notifications_settings.NotificationSettingsViewModel
import com.brokentelephone.game.features.notifications_settings.api.NotificationsSettingsNavigationApiImpl
import com.brokentelephone.game.features.notifications_settings.use_case.GetNotificationsAllowedTypesUseCase
import com.brokentelephone.game.features.notifications_settings.use_case.UpdateNotificationsUseCase
import com.brokentelephone.game.features.notifications_settings.use_case.UpdateUserPermissionsUseCase
import com.brokentelephone.game.notifications_settings_api.NotificationsSettingsNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val notificationsSettingsModule = module {
    factoryOf(::GetNotificationsAllowedTypesUseCase)
    factoryOf(::UpdateNotificationsUseCase)
    viewModelOf(::NotificationSettingsViewModel)
    single<NotificationsSettingsNavigationApi> { NotificationsSettingsNavigationApiImpl() }
    factoryOf(::UpdateUserPermissionsUseCase)
}
