package com.brokentelephone.game.features.post_details.di

import com.brokentelephone.game.domain.use_case.DeletePostUseCase
import com.brokentelephone.game.domain.use_case.GetPostByIdUseCase
import com.brokentelephone.game.features.post_details.PostDetailsViewModel
import com.brokentelephone.game.features.post_details.api.PostDetailsNavigationApiImpl
import com.brokentelephone.game.features.post_details.use_case.JoinSessionUseCase
import com.brokentelephone.game.post_details_api.PostDetailsNavigationApi
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val postDetailsModule = module {
    factoryOf(::GetPostByIdUseCase)
    factoryOf(::DeletePostUseCase)
    factoryOf(::JoinSessionUseCase)
    viewModelOf(::PostDetailsViewModel)
    single<PostDetailsNavigationApi> {
        PostDetailsNavigationApiImpl()
    }
}
