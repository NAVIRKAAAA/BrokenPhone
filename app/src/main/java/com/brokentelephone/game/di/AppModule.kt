package com.brokentelephone.game.di

import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.data.banner.BannerControllerImpl
import com.brokentelephone.game.data.banner.handler.ActiveSessionBannerHandler
import com.brokentelephone.game.data.banner.handler.NewsNotificationBannerHandler
import com.brokentelephone.game.domain.banner.BannerController
import com.brokentelephone.game.domain.use_case.BlockUserUseCase
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetLanguageUseCase
import com.brokentelephone.game.domain.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.domain.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.domain.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.domain.use_case.GetThemeUseCase
import com.brokentelephone.game.domain.use_case.GetUnreadNotificationsCountUseCase
import com.brokentelephone.game.domain.use_case.GetUserCompletedContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserCompletedPostsUseCase
import com.brokentelephone.game.domain.use_case.GetUserContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserPostsUseCase
import com.brokentelephone.game.domain.use_case.GetUsersByIdsUseCase
import com.brokentelephone.game.domain.use_case.HideBannerUseCase
import com.brokentelephone.game.domain.use_case.LogoutUseCase
import com.brokentelephone.game.domain.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.domain.use_case.ObserveBannerUseCase
import com.brokentelephone.game.domain.use_case.ReportPostUseCase
import com.brokentelephone.game.domain.use_case.ReportUserUseCase
import com.brokentelephone.game.domain.use_case.ShowBannerUseCase
import com.brokentelephone.game.domain.use_case.SignInWithGoogleUseCase
import com.brokentelephone.game.main.activity.MainViewModel
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailVerificationUseCase
import com.brokentelephone.game.main.use_case.ApplyPasswordResetUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
    factoryOf(::ActiveSessionBannerHandler)
    factoryOf(::NewsNotificationBannerHandler)
    single<BannerController> { BannerControllerImpl(get(), get(), get()) }
    factoryOf(::ObserveBannerUseCase)
    factoryOf(::ShowBannerUseCase)
    factoryOf(::HideBannerUseCase)
    factoryOf(::CountdownTimer)

    factoryOf(::GetPostLinkByIdUseCase)
    factoryOf(::DeletePostUseCase)
    factoryOf(::ReportPostUseCase)
    factoryOf(::ReportUserUseCase)
    factoryOf(::BlockUserUseCase)
    factoryOf(::MarkPostAsNotInterestedUseCase)
    factoryOf(::SignInWithGoogleUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::GetUnreadNotificationsCountUseCase)
    factoryOf(::GetUsersByIdsUseCase)
    factoryOf(::GetUserPostsUseCase)
    factoryOf(::GetUserContributionsUseCase)
    factoryOf(::GetUserCompletedPostsUseCase)
    factoryOf(::GetUserCompletedContributionsUseCase)

    factoryOf(::LogoutUseCase)
    factoryOf(::GetTermsOfServiceLinkUseCase)
    factoryOf(::GetPrivacyPolicyLinkUseCase)
    factoryOf(::GetLanguageUseCase)
    factoryOf(::GetThemeUseCase)
    factoryOf(::InitializeSessionUseCase)
    factoryOf(::GetActiveSessionUseCase)
    factoryOf(::ApplyEmailChangeUseCase)
    factoryOf(::ApplyEmailVerificationUseCase)
    factoryOf(::ApplyPasswordResetUseCase)
    factoryOf(::GetPendingEmailUseCase)
    viewModelOf(::MainViewModel)
}
