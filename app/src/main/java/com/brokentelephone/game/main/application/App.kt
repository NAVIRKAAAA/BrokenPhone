package com.brokentelephone.game.main.application

import android.app.Application
import com.brokentelephone.game.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(allModules)
        }
    }
}
