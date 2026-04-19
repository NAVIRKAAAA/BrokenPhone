package com.brokentelephone.game.navigation.nav_graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.brokentelephone.game.account_settings_api.AccountSettingsNavigationApi
import com.brokentelephone.game.add_friend_api.AddFriendNavigationApi
import com.brokentelephone.game.blocked_users_api.BlockedUsersNavigationApi
import com.brokentelephone.game.chain_details_api.ChainDetailsNavigationApi
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarNavigationApi
import com.brokentelephone.game.choose_username_api.ChooseUsernameNavigationApi
import com.brokentelephone.game.create_post_api.CreatePostNavigationApi
import com.brokentelephone.game.dashboard_api.DashboardNavigationApi
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.dashboard_api.MainGraph
import com.brokentelephone.game.describe_drawing_api.DescribeDrawingNavigationApi
import com.brokentelephone.game.draw_api.DrawNavigationApi
import com.brokentelephone.game.edit_avatar_api.EditAvatarNavigationApi
import com.brokentelephone.game.edit_bio_api.EditBioNavigationApi
import com.brokentelephone.game.edit_email_api.EditEmailNavigationApi
import com.brokentelephone.game.edit_profile_api.EditProfileNavigationApi
import com.brokentelephone.game.edit_username_api.EditUsernameNavigationApi
import com.brokentelephone.game.features.confirm_sign_up_api.ConfirmSignUpNavigationApi
import com.brokentelephone.game.features.sign_up_api.SignUpNavigationApi
import com.brokentelephone.game.features.welcome_api.WelcomeNavigationApi
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.forgot_password_api.ForgotPasswordNavigationApi
import com.brokentelephone.game.friends_api.FriendsNavigationApi
import com.brokentelephone.game.language_api.LanguageNavigationApi
import com.brokentelephone.game.nav_api.NavigationRoute
import com.brokentelephone.game.new_password_api.NewPasswordNavigationApi
import com.brokentelephone.game.notification_details_api.NotificationDetailsNavigationApi
import com.brokentelephone.game.notifications_api.NotificationsNavigationApi
import com.brokentelephone.game.notifications_settings_api.NotificationsSettingsNavigationApi
import com.brokentelephone.game.post_details_api.PostDetailsNavigationApi
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import com.brokentelephone.game.settings_api.SettingsNavigationApi
import com.brokentelephone.game.sign_in_api.SignInNavigationApi
import com.brokentelephone.game.theme_api.ThemeNavigationApi
import com.brokentelephone.game.user_details_api.UserDetailsNavigationApi
import com.brokentelephone.game.user_friends_api.UserFriendsNavigationApi
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
object AuthGraph : NavigationRoute()

@Composable
fun AppNavGraph(
    startDestination: NavigationRoute,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val welcomeNavigationApi: WelcomeNavigationApi = koinInject()
    val signUpNavigationApi: SignUpNavigationApi = koinInject()
    val confirmSignUpNavigationApi: ConfirmSignUpNavigationApi = koinInject()
    val signInNavigationApi: SignInNavigationApi = koinInject()
    val forgotPasswordNavigationApi: ForgotPasswordNavigationApi = koinInject()
    val chooseAvatarNavigationApi: ChooseAvatarNavigationApi = koinInject()
    val chooseUsernameNavigationApi: ChooseUsernameNavigationApi = koinInject()
    val dashboardNavigationApi: DashboardNavigationApi = koinInject()
    val profileNavigationApi: ProfileNavigationApi = koinInject()
    val postDetailsNavigationApi: PostDetailsNavigationApi = koinInject()
    val drawNavigationApi: DrawNavigationApi = koinInject()
    val describeDrawingNavigationApi: DescribeDrawingNavigationApi = koinInject()
    val chainDetailsNavigationApi: ChainDetailsNavigationApi = koinInject()
    val editProfileNavigationApi: EditProfileNavigationApi = koinInject()
    val editAvatarNavigationApi: EditAvatarNavigationApi = koinInject()
    val editUsernameNavigationApi: EditUsernameNavigationApi = koinInject()
    val editBioNavigationApi: EditBioNavigationApi = koinInject()
    val editEmailNavigationApi: EditEmailNavigationApi = koinInject()
    val createPostNavigationApi: CreatePostNavigationApi = koinInject()
    val settingsNavigationApi: SettingsNavigationApi = koinInject()
    val accountSettingsNavigationApi: AccountSettingsNavigationApi = koinInject()
    val blockedUsersNavigationApi: BlockedUsersNavigationApi = koinInject()
    val languageNavigationApi: LanguageNavigationApi = koinInject()
    val themeNavigationApi: ThemeNavigationApi = koinInject()
    val notificationsNavigationApi: NotificationsNavigationApi = koinInject()
    val notificationsSettingsNavigationApi: NotificationsSettingsNavigationApi = koinInject()
    val userDetailsNavigationApi: UserDetailsNavigationApi = koinInject()
    val friendsNavigationApi: FriendsNavigationApi = koinInject()
    val addFriendNavigationApi: AddFriendNavigationApi = koinInject()
    val userFriendsNavigationApi: UserFriendsNavigationApi = koinInject()
    val notificationDetailsNavigationApi: NotificationDetailsNavigationApi = koinInject()
    val newPasswordNavigationApi: NewPasswordNavigationApi = koinInject()

    val authGraphRoutes = listOf(
        welcomeNavigationApi,
        signUpNavigationApi,
        confirmSignUpNavigationApi,
        signInNavigationApi,
        forgotPasswordNavigationApi,
        chooseAvatarNavigationApi,
        chooseUsernameNavigationApi
    )

    val mainGraphRoutes = listOf(
        dashboardNavigationApi,
        profileNavigationApi,
        postDetailsNavigationApi,
        drawNavigationApi,
        describeDrawingNavigationApi,
        chainDetailsNavigationApi,
        editProfileNavigationApi,
        editAvatarNavigationApi,
        editUsernameNavigationApi,
        editBioNavigationApi,
        editEmailNavigationApi,
        createPostNavigationApi,
        settingsNavigationApi,
        accountSettingsNavigationApi,
        blockedUsersNavigationApi,
        languageNavigationApi,
        themeNavigationApi,
        notificationsNavigationApi,
        notificationsSettingsNavigationApi,
        userDetailsNavigationApi,
        friendsNavigationApi,
        addFriendNavigationApi,
        userFriendsNavigationApi,
        notificationDetailsNavigationApi,
        newPasswordNavigationApi
    )

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        navigation<AuthGraph>(startDestination = WelcomeRoute) {
            authGraphRoutes.forEach {
                it.screen(navController, this)
            }
        }

        navigation<MainGraph>(startDestination = DashboardRoute) {
            mainGraphRoutes.forEach {
                it.screen(navController, this)
            }
        }
    }

}