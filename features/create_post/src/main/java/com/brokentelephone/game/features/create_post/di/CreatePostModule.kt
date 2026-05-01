package com.brokentelephone.game.features.create_post.di

import com.brokentelephone.game.create_post_api.CreatePostNavigationApi
import com.brokentelephone.game.features.create_post.CreatePostViewModel
import com.brokentelephone.game.features.create_post.api.CreatePostNavigationApiImpl
import com.brokentelephone.game.features.create_post.use_case.CreatePostUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val createPostModule = module {
    factoryOf(::CreatePostUseCase)
    viewModelOf(::CreatePostViewModel)
    single<CreatePostNavigationApi> { CreatePostNavigationApiImpl() }
}
