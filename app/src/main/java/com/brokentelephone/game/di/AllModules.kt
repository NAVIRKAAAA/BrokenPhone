package com.brokentelephone.game.di

import com.brokentelephone.game.essentials.di.essentialsModule
import com.brokentelephone.game.features.account_settings.di.accountSettingsModule
import com.brokentelephone.game.features.add_friend.di.addFriendModule
import com.brokentelephone.game.features.blocked_users.di.blockedUsersModule
import com.brokentelephone.game.features.bottom_nav_bar.di.bottomNavBarModule
import com.brokentelephone.game.features.chain_details.di.chainDetailsModule
import com.brokentelephone.game.features.choose_avatar.di.chooseAvatarModule
import com.brokentelephone.game.features.choose_username.di.chooseUsernameModule
import com.brokentelephone.game.features.confirm_sign_up.di.confirmSignUpModule
import com.brokentelephone.game.features.create_post.di.createPostModule
import com.brokentelephone.game.features.dashboard.di.dashboardModule
import com.brokentelephone.game.features.describe_drawing.di.describeDrawingModule
import com.brokentelephone.game.features.draw.di.drawModule
import com.brokentelephone.game.features.edit_avatar.di.editAvatarModule
import com.brokentelephone.game.features.edit_bio.di.editBioModule
import com.brokentelephone.game.features.edit_email.di.editEmailModule
import com.brokentelephone.game.features.edit_profile.di.editProfileModule
import com.brokentelephone.game.features.edit_username.di.editUsernameModule
import com.brokentelephone.game.features.forgot_password.di.forgotPasswordModule
import com.brokentelephone.game.features.friends.di.friendsModule
import com.brokentelephone.game.features.language.di.languageModule
import com.brokentelephone.game.features.new_password.di.newPasswordModule
import com.brokentelephone.game.features.notification_details.di.notificationDetailsModule
import com.brokentelephone.game.features.notifications.di.notificationsModule
import com.brokentelephone.game.features.notifications_settings.di.notificationsSettingsModule
import com.brokentelephone.game.features.post_details.di.postDetailsModule
import com.brokentelephone.game.features.profile.di.profileModule
import com.brokentelephone.game.features.settings.di.settingsModule
import com.brokentelephone.game.features.sign_in.di.signInModule
import com.brokentelephone.game.features.sign_up.di.signUpModule
import com.brokentelephone.game.features.theme.di.themeModule
import com.brokentelephone.game.features.user_details.di.userDetailsModule
import com.brokentelephone.game.features.user_friends.di.userFriendsModule
import com.brokentelephone.game.features.welcome.di.welcomeModule

val allModules = listOf(
    essentialsModule,
    dataModule,
    appModule,
    welcomeModule,
    signInModule,
    signUpModule,
    confirmSignUpModule,
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
    notificationDetailsModule,
    notificationsSettingsModule,
    profileModule,
    postDetailsModule,
    drawModule,
    describeDrawingModule,
    chainDetailsModule,
    editProfileModule,
    editAvatarModule,
    editUsernameModule,
    createPostModule,
    settingsModule,
    accountSettingsModule,
    blockedUsersModule,
    languageModule,
    themeModule,
    newPasswordModule,
)
