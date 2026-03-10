package com.brokentelephone.game.main

import android.content.Context
import androidx.startup.Initializer
import com.google.firebase.FirebaseApp

class FirebaseInitializer : Initializer<FirebaseApp> {

    override fun create(context: Context): FirebaseApp {
        return FirebaseApp.initializeApp(context)!!
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}