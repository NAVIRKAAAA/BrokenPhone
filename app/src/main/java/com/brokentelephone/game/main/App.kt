package com.brokentelephone.game.main

import android.app.Application
import com.brokentelephone.game.di.appModule
import com.brokentelephone.game.essentials.di.essentialsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(essentialsModule, appModule)
        }
    }
}
