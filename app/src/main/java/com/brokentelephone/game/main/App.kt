package com.brokentelephone.game.main

import android.app.Application
import com.brokentelephone.game.di.appModule
import com.brokentelephone.game.essentials.di.essentialsModule
import com.brokentelephone.game.features.add_friend.di.addFriendModule
import com.brokentelephone.game.features.edit_bio.di.editBioModule
import com.brokentelephone.game.features.edit_email.di.editEmailModule
import com.brokentelephone.game.features.forgot_password.di.forgotPasswordModule
import com.brokentelephone.game.features.friends.di.friendsModule
import com.brokentelephone.game.features.sign_up.di.signUpModule
import com.brokentelephone.game.features.user_details.di.userDetailsModule
import com.brokentelephone.game.features.welcome.di.welcomeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(essentialsModule, appModule, welcomeModule, signUpModule, forgotPasswordModule, editBioModule, editEmailModule, userDetailsModule, friendsModule, addFriendModule)
        }
    }
}
