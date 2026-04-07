package com.brokentelephone.game.features.notification_details.di

import com.brokentelephone.game.features.notification_details.NotificationDetailsViewModel
import com.brokentelephone.game.features.notification_details.api.NotificationDetailsNavigationApiImpl
import com.brokentelephone.game.notification_details_api.NotificationDetailsNavigationApi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val notificationDetailsModule = module {
    viewModel { (notificationId: String) -> NotificationDetailsViewModel(notificationId) }
    single<NotificationDetailsNavigationApi> { NotificationDetailsNavigationApiImpl() }
}
