package com.brokentelephone.game.di

import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.domain.use_case.BlockUserUseCase
import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetAuthStateUseCase
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.domain.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.domain.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.domain.use_case.GetUnreadNotificationsCountUseCase
import com.brokentelephone.game.domain.use_case.GetUserContributionsUseCase
import com.brokentelephone.game.domain.use_case.GetUserPostsUseCase
import com.brokentelephone.game.domain.use_case.GetUsersByIdsUseCase
import com.brokentelephone.game.domain.use_case.LogoutUseCase
import com.brokentelephone.game.domain.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.domain.use_case.ReportPostUseCase
import com.brokentelephone.game.domain.use_case.ReportUserUseCase
import com.brokentelephone.game.domain.use_case.SignInWithGoogleUseCase
import com.brokentelephone.game.features.account_settings.AccountSettingsViewModel
import com.brokentelephone.game.features.account_settings.use_case.DeleteAccountUseCase
import com.brokentelephone.game.features.account_settings.use_case.SendEmailVerificationUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetLanguageUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetThemeUseCase
import com.brokentelephone.game.features.blocked_users.BlockedUsersViewModel
import com.brokentelephone.game.features.blocked_users.use_case.GetBlockedUsersUseCase
import com.brokentelephone.game.features.blocked_users.use_case.UnblockUserUseCase
import com.brokentelephone.game.features.chain_details.ChainDetailsViewModel
import com.brokentelephone.game.features.chain_details.use_case.GetChainByPostIdUseCase
import com.brokentelephone.game.features.create_post.CreatePostViewModel
import com.brokentelephone.game.features.create_post.use_case.CreatePostUseCase
import com.brokentelephone.game.features.describe_drawing.DescribeDrawingViewModel
import com.brokentelephone.game.features.describe_drawing.use_case.SubmitDescriptionUseCase
import com.brokentelephone.game.features.draw.DrawViewModel
import com.brokentelephone.game.features.draw.use_case.CancelSessionUseCase
import com.brokentelephone.game.features.draw.use_case.SubmitDrawingUseCase
import com.brokentelephone.game.features.draw.utils.DrawingBitmapSaver
import com.brokentelephone.game.features.edit_avatar.EditAvatarViewModel
import com.brokentelephone.game.features.edit_avatar.use_case.UpdateAvatarUseCase
import com.brokentelephone.game.features.edit_profile.EditProfileViewModel
import com.brokentelephone.game.features.edit_username.EditUsernameViewModel
import com.brokentelephone.game.features.edit_username.use_case.UpdateUsernameUseCase
import com.brokentelephone.game.features.language.LanguageViewModel
import com.brokentelephone.game.features.language.use_case.SetupFirstAppLaunchUseCase
import com.brokentelephone.game.features.language.use_case.UpdateLanguageUseCase
import com.brokentelephone.game.features.notifications.NotificationSettingsViewModel
import com.brokentelephone.game.features.notifications.use_case.GetNotificationsAllowedTypesUseCase
import com.brokentelephone.game.features.notifications.use_case.UpdateNotificationsUseCase
import com.brokentelephone.game.features.post_details.PostDetailsViewModel
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.JoinSessionUseCase
import com.brokentelephone.game.features.profile.ProfileViewModel
import com.brokentelephone.game.features.settings.SettingsViewModel
import com.brokentelephone.game.features.settings.use_case.GetVersionInfoUseCase
import com.brokentelephone.game.features.theme.ThemeViewModel
import com.brokentelephone.game.features.theme.use_case.UpdateThemeUseCase
import com.brokentelephone.game.main.MainViewModel
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailVerificationUseCase
import com.brokentelephone.game.main.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.main.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { DrawingBitmapSaver(androidContext()) }
    factoryOf(::CountdownTimer)

    factoryOf(::GetPostByIdUseCase)
    factoryOf(::GetPostLinkByIdUseCase)
    factoryOf(::DeletePostUseCase)
    factoryOf(::ReportPostUseCase)
    factoryOf(::ReportUserUseCase)
    factoryOf(::BlockUserUseCase)
    factoryOf(::JoinSessionUseCase)
    factoryOf(::MarkPostAsNotInterestedUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::SubmitDrawingUseCase)
    factoryOf(::SubmitDescriptionUseCase)
    factoryOf(::CancelSessionUseCase)
    factoryOf(::GetChainByPostIdUseCase)
    factoryOf(::SignInWithGoogleUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::GetUnreadNotificationsCountUseCase)
    factoryOf(::GetUsersByIdsUseCase)
    factoryOf(::UpdateUsernameUseCase)
    factoryOf(::UpdateAvatarUseCase)
    factoryOf(::GetUserPostsUseCase)
    factoryOf(::GetUserContributionsUseCase)

    viewModelOf(::CreatePostViewModel)
    viewModelOf(::PostDetailsViewModel)
    viewModelOf(::DrawViewModel)
    viewModelOf(::DescribeDrawingViewModel)
    viewModelOf(::ChainDetailsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditUsernameViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::EditAvatarViewModel)

    factoryOf(::GetVersionInfoUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetAuthStateUseCase)
    viewModelOf(::SettingsViewModel)
    factoryOf(::DeleteAccountUseCase)
    factoryOf(::SendEmailVerificationUseCase)
    factoryOf(::GetBlockedUsersUseCase)
    factoryOf(::UnblockUserUseCase)
    viewModelOf(::BlockedUsersViewModel)
    viewModelOf(::AccountSettingsViewModel)
    factoryOf(::GetTermsOfServiceLinkUseCase)
    factoryOf(::GetPrivacyPolicyLinkUseCase)
    factoryOf(::GetLanguageUseCase)
    factoryOf(::GetThemeUseCase)
    factoryOf(::GetNotificationsAllowedTypesUseCase)
    factoryOf(::UpdateNotificationsUseCase)
    viewModelOf(::NotificationSettingsViewModel)
    factoryOf(::UpdateLanguageUseCase)
    factoryOf(::SetupFirstAppLaunchUseCase)
    viewModelOf(::LanguageViewModel)
    factoryOf(::UpdateThemeUseCase)
    viewModelOf(::ThemeViewModel)

    factoryOf(::InitializeSessionUseCase)
    factoryOf(::GetActiveSessionUseCase)
    factoryOf(::ApplyEmailChangeUseCase)
    factoryOf(::ApplyEmailVerificationUseCase)
    factoryOf(::GetPendingEmailUseCase)
    viewModelOf(::MainViewModel)
}
