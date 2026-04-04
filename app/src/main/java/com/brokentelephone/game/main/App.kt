package com.brokentelephone.game.main

import android.app.Application
import com.brokentelephone.game.di.appModule
import com.brokentelephone.game.di.dataModule
import com.brokentelephone.game.essentials.di.essentialsModule
import com.brokentelephone.game.features.add_friend.di.addFriendModule
import com.brokentelephone.game.features.bottom_nav_bar.di.bottomNavBarModule
import com.brokentelephone.game.features.chain_details.di.chainDetailsModule
import com.brokentelephone.game.features.choose_avatar.di.chooseAvatarModule
import com.brokentelephone.game.features.choose_username.di.chooseUsernameModule
import com.brokentelephone.game.features.dashboard.di.dashboardModule
import com.brokentelephone.game.features.describe_drawing.di.describeDrawingModule
import com.brokentelephone.game.features.draw.di.drawModule
import com.brokentelephone.game.features.edit_bio.di.editBioModule
import com.brokentelephone.game.features.edit_email.di.editEmailModule
import com.brokentelephone.game.features.edit_profile.di.editProfileModule
import com.brokentelephone.game.features.forgot_password.di.forgotPasswordModule
import com.brokentelephone.game.features.friends.di.friendsModule
import com.brokentelephone.game.features.notifications.di.notificationsModule
import com.brokentelephone.game.features.post_details.di.postDetailsModule
import com.brokentelephone.game.features.profile.di.profileModule
import com.brokentelephone.game.features.sign_in.di.signInModule
import com.brokentelephone.game.features.sign_up.di.signUpModule
import com.brokentelephone.game.features.user_details.di.userDetailsModule
import com.brokentelephone.game.features.user_friends.di.userFriendsModule
import com.brokentelephone.game.features.welcome.di.welcomeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                essentialsModule,
                dataModule,
                appModule,
                welcomeModule,
                signInModule,
                signUpModule,
                chooseAvatarModule,
                chooseUsernameModule,
                dashboardModule,
                bottomNavBarModule,
                forgotPasswordModule,
                editBioModule,
                editEmailModule,
                userDetailsModule,
                friendsModule,
                userFriendsModule,
                addFriendModule,
                notificationsModule,
                profileModule,
                postDetailsModule,
                drawModule,
                describeDrawingModule,
                chainDetailsModule,
                editProfileModule
            )
        }
    }
}
