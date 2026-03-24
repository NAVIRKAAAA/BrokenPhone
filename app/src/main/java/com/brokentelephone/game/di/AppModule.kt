package com.brokentelephone.game.di

import com.brokentelephone.game.core.timer.CountdownTimer
import com.brokentelephone.game.data.google.GoogleSignInManagerImpl
import com.brokentelephone.game.data.handler.ApiHandlerImpl
import com.brokentelephone.game.data.link.MockLinkProviderImpl
import com.brokentelephone.game.data.repository.AuthRepositoryImpl
import com.brokentelephone.game.data.repository.GameSessionRepositoryImpl
import com.brokentelephone.game.data.repository.MockAppInfoRepositoryImpl
import com.brokentelephone.game.data.repository.PostsRepositoryImpl
import com.brokentelephone.game.data.repository.ReportsRepositoryImpl
import com.brokentelephone.game.data.repository.UserSettingsRepositoryImpl
import com.brokentelephone.game.data.repository.UsersRepositoryImpl
import com.brokentelephone.game.data.session.UserSessionImpl
import com.brokentelephone.game.data.storage.FirebaseImageStorage
import com.brokentelephone.game.domain.api_handler.ApiHandler
import com.brokentelephone.game.domain.google.GoogleSignInManager
import com.brokentelephone.game.domain.link.LinkProvider
import com.brokentelephone.game.domain.repository.AppInfoRepository
import com.brokentelephone.game.domain.repository.AuthRepository
import com.brokentelephone.game.domain.repository.GameSessionRepository
import com.brokentelephone.game.domain.repository.PostRepository
import com.brokentelephone.game.domain.repository.ReportsRepository
import com.brokentelephone.game.domain.repository.UserSettingsRepository
import com.brokentelephone.game.domain.repository.UsersRepository
import com.brokentelephone.game.domain.storage.ImageStorage
import com.brokentelephone.game.domain.use_case.GetCurrentUserUseCase
import com.brokentelephone.game.domain.use_case.GetUsersByIdsUseCase
import com.brokentelephone.game.domain.use_case.LogoutUseCase
import com.brokentelephone.game.domain.use_case.SignInWithGoogleUseCase
import com.brokentelephone.game.domain.user.UserSession
import com.brokentelephone.game.essentials.validation.SignUpValidator
import com.brokentelephone.game.features.account_settings.AccountSettingsViewModel
import com.brokentelephone.game.features.account_settings.use_case.DeleteAccountUseCase
import com.brokentelephone.game.features.account_settings.use_case.SendEmailVerificationUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetLanguageUseCase
import com.brokentelephone.game.features.app_preferences.use_case.GetThemeUseCase
import com.brokentelephone.game.features.blocked_users.BlockedUsersViewModel
import com.brokentelephone.game.features.blocked_users.use_case.GetBlockedUsersUseCase
import com.brokentelephone.game.features.blocked_users.use_case.UnblockUserUseCase
import com.brokentelephone.game.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.brokentelephone.game.features.chain_details.ChainDetailsViewModel
import com.brokentelephone.game.features.chain_details.use_case.GetChainByPostIdUseCase
import com.brokentelephone.game.features.choose_avatar.ChooseAvatarViewModel
import com.brokentelephone.game.features.choose_avatar.use_case.CompleteAvatarStepUseCase
import com.brokentelephone.game.features.choose_username.ChooseUsernameViewModel
import com.brokentelephone.game.features.choose_username.use_case.CompleteUsernameStepUseCase
import com.brokentelephone.game.features.create_post.CreatePostViewModel
import com.brokentelephone.game.features.create_post.use_case.CreatePostUseCase
import com.brokentelephone.game.features.dashboard.DashboardViewModel
import com.brokentelephone.game.features.dashboard.use_case.LoadInitialPostsUseCase
import com.brokentelephone.game.features.dashboard.use_case.LoadNextPostsUseCase
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
import com.brokentelephone.game.features.language.use_case.InitializeLanguageUseCase
import com.brokentelephone.game.features.language.use_case.UpdateLanguageUseCase
import com.brokentelephone.game.features.notifications.NotificationsViewModel
import com.brokentelephone.game.features.notifications.use_case.GetNotificationsUseCase
import com.brokentelephone.game.features.notifications.use_case.UpdateNotificationsUseCase
import com.brokentelephone.game.features.post_details.PostDetailsViewModel
import com.brokentelephone.game.features.post_details.use_case.BlockUserUseCase
import com.brokentelephone.game.features.post_details.use_case.DeletePostUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.GetPostLinkByIdUseCase
import com.brokentelephone.game.features.post_details.use_case.JoinSessionUseCase
import com.brokentelephone.game.features.post_details.use_case.MarkPostAsNotInterestedUseCase
import com.brokentelephone.game.features.post_details.use_case.ReportPostUseCase
import com.brokentelephone.game.features.profile.ProfileViewModel
import com.brokentelephone.game.features.profile.use_case.GetContributionsUseCase
import com.brokentelephone.game.features.profile.use_case.GetMyPostsUseCase
import com.brokentelephone.game.features.settings.SettingsViewModel
import com.brokentelephone.game.features.settings.use_case.GetAuthStateUseCase
import com.brokentelephone.game.features.settings.use_case.GetPrivacyPolicyLinkUseCase
import com.brokentelephone.game.features.settings.use_case.GetTermsOfServiceLinkUseCase
import com.brokentelephone.game.features.settings.use_case.GetVersionInfoUseCase
import com.brokentelephone.game.features.sign_in.SignInViewModel
import com.brokentelephone.game.features.sign_in.use_case.SignInWithEmailPasswordUseCase
import com.brokentelephone.game.features.sign_up.SignUpViewModel
import com.brokentelephone.game.features.sign_up.use_case.SignUpUseCase
import com.brokentelephone.game.features.sign_up.use_case.ValidateSignUpUseCase
import com.brokentelephone.game.features.theme.ThemeViewModel
import com.brokentelephone.game.features.theme.use_case.UpdateThemeUseCase
import com.brokentelephone.game.features.welcome.WelcomeViewModel
import com.brokentelephone.game.features.welcome.use_case.SignInAnonymouslyUseCase
import com.brokentelephone.game.main.MainViewModel
import com.brokentelephone.game.main.use_case.ApplyEmailChangeUseCase
import com.brokentelephone.game.main.use_case.ApplyEmailVerificationUseCase
import com.brokentelephone.game.main.use_case.GetActiveSessionUseCase
import com.brokentelephone.game.main.use_case.GetPendingEmailUseCase
import com.brokentelephone.game.main.use_case.InitializeSessionUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.storage.FirebaseStorage
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<LinkProvider> { MockLinkProviderImpl() }
    single<AppInfoRepository> { MockAppInfoRepositoryImpl() }
    single { FirebaseStorage.getInstance() }
    single<ImageStorage> { FirebaseImageStorage(get()) }
    single<PostRepository> { PostsRepositoryImpl(get()) }
    single<GameSessionRepository> { GameSessionRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), androidContext()) }
    single<GoogleSignInManager> { GoogleSignInManagerImpl(androidContext()) }
    single { FirebaseAuth.getInstance() }
    single {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = firestoreSettings {
                setLocalCacheSettings(memoryCacheSettings {})
            }
        }
    }
    single<ApiHandler> { ApiHandlerImpl() }
    single<UserSession> { UserSessionImpl(get(), get()) }
    single<ReportsRepository> { ReportsRepositoryImpl(get()) }
    single<UserSettingsRepository> { UserSettingsRepositoryImpl(androidContext()) }
    single<UsersRepository> { UsersRepositoryImpl(get()) }

    single { DrawingBitmapSaver(androidContext()) }
    factoryOf(::CountdownTimer)

    factoryOf(::GetPostByIdUseCase)
    factoryOf(::GetPostLinkByIdUseCase)
    factoryOf(::DeletePostUseCase)
    factoryOf(::ReportPostUseCase)
    factoryOf(::BlockUserUseCase)
    factoryOf(::JoinSessionUseCase)
    factoryOf(::MarkPostAsNotInterestedUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::SubmitDrawingUseCase)
    factoryOf(::SubmitDescriptionUseCase)
    factoryOf(::CancelSessionUseCase)
    factoryOf(::GetChainByPostIdUseCase)
    factoryOf(::SignInAnonymouslyUseCase)
    viewModelOf(::WelcomeViewModel)
    factoryOf(::SignUpValidator)
    factoryOf(::ValidateSignUpUseCase)
    factoryOf(::SignUpUseCase)
    factoryOf(::SignInWithEmailPasswordUseCase)
    factoryOf(::SignInWithGoogleUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::GetUsersByIdsUseCase)
    factoryOf(::UpdateUsernameUseCase)
    factoryOf(::UpdateAvatarUseCase)
    factoryOf(::GetMyPostsUseCase)
    factoryOf(::GetContributionsUseCase)

    viewModelOf(::CreatePostViewModel)
    factoryOf(::LoadInitialPostsUseCase)
    factoryOf(::LoadNextPostsUseCase)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::PostDetailsViewModel)
    viewModelOf(::DrawViewModel)
    viewModelOf(::DescribeDrawingViewModel)
    viewModelOf(::ChainDetailsViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::EditUsernameViewModel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::ChooseUsernameViewModel)
    factoryOf(::CompleteAvatarStepUseCase)
    factoryOf(::CompleteUsernameStepUseCase)
    viewModelOf(::ChooseAvatarViewModel)
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
    factoryOf(::GetNotificationsUseCase)
    factoryOf(::UpdateNotificationsUseCase)
    viewModelOf(::NotificationsViewModel)
    factoryOf(::UpdateLanguageUseCase)
    factoryOf(::InitializeLanguageUseCase)
    viewModelOf(::LanguageViewModel)
    factoryOf(::UpdateThemeUseCase)
    viewModelOf(::ThemeViewModel)

    factoryOf(::InitializeSessionUseCase)
    factoryOf(::GetActiveSessionUseCase)
    factoryOf(::ApplyEmailChangeUseCase)
    factoryOf(::ApplyEmailVerificationUseCase)
    factoryOf(::GetPendingEmailUseCase)
    single { AppNavBottomBarViewModel(get(), get()) }
    viewModelOf(::MainViewModel)
}
