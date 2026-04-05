package com.brokentelephone.game.navigation.nav_graph

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.brokentelephone.game.account_settings_api.AccountSettingsNavigationApi
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
import com.brokentelephone.game.features.add_friend.AddFriendScreen
import com.brokentelephone.game.features.sign_up_api.SignUpNavigationApi
import com.brokentelephone.game.features.user_friends.UserFriendsScreen
import com.brokentelephone.game.features.welcome_api.WelcomeNavigationApi
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.forgot_password_api.ForgotPasswordNavigationApi
import com.brokentelephone.game.friends_api.FriendsNavigationApi
import com.brokentelephone.game.language_api.LanguageNavigationApi
import com.brokentelephone.game.nav_api.NavigationRoute
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.navigation.routes.Routes
import com.brokentelephone.game.notifications_api.NotificationsNavigationApi
import com.brokentelephone.game.notifications_settings_api.NotificationsSettingsNavigationApi
import com.brokentelephone.game.post_details_api.PostDetailsNavigationApi
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import com.brokentelephone.game.settings_api.SettingsNavigationApi
import com.brokentelephone.game.sign_in_api.SignInNavigationApi
import com.brokentelephone.game.theme_api.ThemeNavigationApi
import com.brokentelephone.game.user_details_api.UserDetailsNavigationApi
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
object AuthGraph : NavigationRoute()

@Composable
fun AppNavGraph(
    startDestination: NavigationRoute,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onBannerDismissed: () -> Unit = {},
) {
    val welcomeNavigationApi: WelcomeNavigationApi = koinInject()
    val signUpNavigationApi: SignUpNavigationApi = koinInject()
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

    val authGraphRoutes = listOf(
        welcomeNavigationApi,
        signUpNavigationApi,
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
        friendsNavigationApi
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

            composable<Routes.AddFriend>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    if (targetState.destination.route?.contains("UserDetails") == true) {
                        ExitTransition.None
                    } else {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    }
                },
                popEnterTransition = {
                    if (initialState.destination.route?.contains("UserDetails") == true) {
                        EnterTransition.None
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    }
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                AddFriendScreen(
                    onBackClick = navController::safePopBackStack,
                    onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                    },
                )
            }

            composable<Routes.UserFriends>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.UserFriends>()
                UserFriendsScreen(
                    userId = route.userId,
                    onBackClick = navController::safePopBackStack,
                    onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                    },
                )
            }

        }
    }
}

// 1111