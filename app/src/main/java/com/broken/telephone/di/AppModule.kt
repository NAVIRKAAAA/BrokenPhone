package com.broken.telephone.di

import com.broken.telephone.core.timer.CountdownTimer
import com.broken.telephone.data.repository.MockAuthRepository
import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.data.session.MockUserSessionImpl
import com.broken.telephone.domain.repository.AuthRepository
import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.domain.user.UserSession
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.chain_details.ChainDetailsViewModel
import com.broken.telephone.features.chain_details.use_case.GetChainByPostIdUseCase
import com.broken.telephone.features.create_post.CreatePostViewModel
import com.broken.telephone.features.create_post.use_case.CreatePostUseCase
import com.broken.telephone.features.dashboard.DashboardViewModel
import com.broken.telephone.features.dashboard.use_case.GetPostsUseCase
import com.broken.telephone.features.describe_drawing.DescribeDrawingViewModel
import com.broken.telephone.features.describe_drawing.use_case.SubmitDescriptionUseCase
import com.broken.telephone.features.draw.DrawViewModel
import com.broken.telephone.features.draw.use_case.SubmitDrawingUseCase
import com.broken.telephone.features.draw.utils.DrawingBitmapSaver
import com.broken.telephone.features.edit_profile.EditProfileViewModel
import com.broken.telephone.features.edit_username.EditUsernameViewModel
import com.broken.telephone.features.edit_username.use_case.UpdateProfileUseCase
import com.broken.telephone.features.post_details.PostDetailsViewModel
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import com.broken.telephone.features.profile.ProfileViewModel
import com.broken.telephone.features.profile.use_case.GetCurrentUserUseCase
import com.broken.telephone.features.profile.use_case.GetMyContributionsUseCase
import com.broken.telephone.features.profile.use_case.GetMyPostsUseCase
import com.broken.telephone.features.sign_in.SignInViewModel
import com.broken.telephone.features.sign_in.use_case.SignInUseCase
import com.broken.telephone.features.sign_up.SignUpValidator
import com.broken.telephone.features.sign_up.SignUpViewModel
import com.broken.telephone.features.sign_up.use_case.SignUpUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<PostRepository> { MockPostRepository() }
    single<AuthRepository> { MockAuthRepository() }
    single<UserSession> { MockUserSessionImpl() }

    single { DrawingBitmapSaver(androidContext()) }
    factoryOf(::CountdownTimer)

    factoryOf(::GetPostsUseCase)
    factoryOf(::GetPostByIdUseCase)
    factoryOf(::CreatePostUseCase)
    factoryOf(::SubmitDrawingUseCase)
    factoryOf(::SubmitDescriptionUseCase)
    factoryOf(::GetChainByPostIdUseCase)
    factoryOf(::SignUpValidator)
    factoryOf(::SignUpUseCase)
    factoryOf(::SignInUseCase)
    factoryOf(::GetCurrentUserUseCase)
    factoryOf(::UpdateProfileUseCase)
    factoryOf(::GetMyPostsUseCase)
    factoryOf(::GetMyContributionsUseCase)

    viewModelOf(::CreatePostViewModel)
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

    single { AppNavBottomBarViewModel() }
}
