package com.brokentelephone.game.features.draw.di

import com.brokentelephone.game.draw_api.DrawNavigationApi
import com.brokentelephone.game.features.draw.DrawViewModel
import com.brokentelephone.game.features.draw.api.DrawNavigationApiImpl
import com.brokentelephone.game.features.draw.use_case.CancelSessionUseCase
import com.brokentelephone.game.features.draw.use_case.SubmitDrawingUseCase
import com.brokentelephone.game.features.draw.utils.DrawingBitmapSaver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val drawModule = module {
    single { DrawingBitmapSaver(androidContext()) }
    factoryOf(::SubmitDrawingUseCase)
    factoryOf(::CancelSessionUseCase)
    viewModelOf(::DrawViewModel)
    single<DrawNavigationApi> { DrawNavigationApiImpl() }
}
