package com.brokentelephone.game.essentials.di

import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapper
import com.brokentelephone.game.essentials.exceptions.main.ExceptionToMessageMapperImpl
import com.brokentelephone.game.essentials.exceptions.main.StringProvider
import com.brokentelephone.game.essentials.exceptions.main.StringProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val essentialsModule = module {
    single<StringProvider> { StringProviderImpl(androidContext()) }
    single<ExceptionToMessageMapper> { ExceptionToMessageMapperImpl(get()) }
}
