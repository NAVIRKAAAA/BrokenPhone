package com.brokentelephone.game.features.describe_drawing.di

import com.brokentelephone.game.describe_drawing_api.DescribeDrawingNavigationApi
import com.brokentelephone.game.features.describe_drawing.DescribeDrawingViewModel
import com.brokentelephone.game.features.describe_drawing.api.DescribeDrawingNavigationApiImpl
import com.brokentelephone.game.features.describe_drawing.use_case.SubmitDescriptionUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val describeDrawingModule = module {
    factoryOf(::SubmitDescriptionUseCase)
    viewModelOf(::DescribeDrawingViewModel)
    single<DescribeDrawingNavigationApi> { DescribeDrawingNavigationApiImpl() }
}
