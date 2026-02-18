package com.broken.telephone.di

import com.broken.telephone.data.repository.MockPostRepository
import com.broken.telephone.domain.repository.PostRepository
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBarViewModel
import com.broken.telephone.features.create_post.CreatePostViewModel
import com.broken.telephone.features.dashboard.DashboardViewModel
import com.broken.telephone.features.dashboard.use_case.GetPostsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<PostRepository> { MockPostRepository() }

    factoryOf(::GetPostsUseCase)

    viewModelOf(::CreatePostViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AppNavBottomBarViewModel)
}
