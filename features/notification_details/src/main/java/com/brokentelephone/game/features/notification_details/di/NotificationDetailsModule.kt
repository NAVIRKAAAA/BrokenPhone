package com.brokentelephone.game.features.notification_details.di

import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetNotificationByIdUseCase
import com.brokentelephone.game.domain.use_case.MarkNotificationAsReadUseCase
import com.brokentelephone.game.features.notification_details.NotificationDetailsViewModel
import com.brokentelephone.game.features.notification_details.api.NotificationDetailsNavigationApiImpl
import com.brokentelephone.game.notification_details_api.NotificationDetailsNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val notificationDetailsModule = module {
    viewModel { (notificationId: String) ->
        NotificationDetailsViewModel(notificationId, get(), get())
    }
    single<NotificationDetailsNavigationApi> { NotificationDetailsNavigationApiImpl() }
    factoryOf(::GetNotificationByIdUseCase)
    factoryOf(::MarkNotificationAsReadUseCase)
    factoryOf(::GetCurrentUserUseCase)
}
