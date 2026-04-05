package com.brokentelephone.game.di

import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.domain.use_case.BlockUserUseCase
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetLanguageUseCase
import com.brokentelephone.game.domain.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.domain.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.domain.use_case.GetThemeUseCase
import com.brokentelephone.game.domain.use_case.GetUnreadNotificationsCountUseCase
import com.brokentelephone.game.domain.use_case.GetUserContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserPostsUseCase
import com.brokentelephone.game.domain.use_case.GetUsersByIdsUseCase
import com.brokentelephone.game.domain.use_case.LogoutUseCase
import com.brokentelephone.game.domain.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.domain.use_case.ReportPostUseCase
import com.brokentelephone.game.domain.use_case.ReportUserUseCase
import com.brokentelephone.game.domain.use_case.SignInWithGoogleUseCase
import com.brokentelephone.game.main.MainViewModel
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailVerificationUseCase
import com.brokentelephone.game.main.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
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

    factoryOf(::LogoutUseCase)
    factoryOf(::GetTermsOfServiceLinkUseCase)
    factoryOf(::GetPrivacyPolicyLinkUseCase)
    factoryOf(::GetLanguageUseCase)
    factoryOf(::GetThemeUseCase)
    factoryOf(::InitializeSessionUseCase)
    factoryOf(::GetActiveSessionUseCase)
    factoryOf(::ApplyEmailChangeUseCase)
    factoryOf(::ApplyEmailVerificationUseCase)
    factoryOf(::GetPendingEmailUseCase)
    viewModelOf(::MainViewModel)
}
