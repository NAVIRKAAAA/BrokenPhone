package com.broken.telephone.di

import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.create_post.CreatePostViewModel
import com.broken.telephone.features.dashboard.DashboardViewModel
import com.broken.telephone.features.dashboard.use_case.GetPostsUseCase
import com.broken.telephone.features.draw.DrawViewModel
import com.broken.telephone.features.draw.use_case.SubmitDrawingUseCase
import com.broken.telephone.features.draw.utils.DrawingBitmapSaver
import com.broken.telephone.features.post_details.PostDetailsViewModel
import com.broken.telephone.features.post_details.use_case.GetPostByIdUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<PostRepository> { MockPostRepository() }

    single { DrawingBitmapSaver(androidContext()) }

    factoryOf(::GetPostsUseCase)
    factoryOf(::GetPostByIdUseCase)
    factoryOf(::SubmitDrawingUseCase)

    viewModelOf(::CreatePostViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AppNavBottomBarViewModel)
    viewModelOf(::PostDetailsViewModel)
    viewModelOf(::DrawViewModel)
}
