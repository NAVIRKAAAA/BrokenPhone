package com.brokentelephone.game.navigation.nav_graph

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.brokentelephone.game.choose_avatar_api.ChooseAvatarNavigationApi
import com.brokentelephone.game.choose_username_api.ChooseUsernameNavigationApi
import com.brokentelephone.game.dashboard_api.DashboardNavigationApi
import com.brokentelephone.game.dashboard_api.DashboardRoute
import com.brokentelephone.game.dashboard_api.MainGraph
import com.brokentelephone.game.draw_api.DrawNavigationApi
import com.brokentelephone.game.features.account_settings.AccountSettingsScreen
import com.brokentelephone.game.features.add_friend.AddFriendScreen
import com.brokentelephone.game.features.blocked_users.BlockedUsersScreen
import com.brokentelephone.game.features.chain_details.ChainDetailsScreen
import com.brokentelephone.game.features.chain_details.ChainDetailsViewModel
import com.brokentelephone.game.features.create_post.CreatePostScreen
import com.brokentelephone.game.features.describe_drawing.DescribeDrawingScreen
import com.brokentelephone.game.features.edit_avatar.EditAvatarScreen
import com.brokentelephone.game.features.edit_bio.EditBioScreen
import com.brokentelephone.game.features.edit_email.EditEmailScreen
import com.brokentelephone.game.features.edit_profile.EditProfileScreen
import com.brokentelephone.game.features.edit_username.EditUsernameScreen
import com.brokentelephone.game.features.friends.FriendsScreen
import com.brokentelephone.game.features.language.LanguageScreen
import com.brokentelephone.game.features.notifications.NotificationSettingsScreen
import com.brokentelephone.game.features.notifications.NotificationsScreen
import com.brokentelephone.game.features.settings.SettingsScreen
import com.brokentelephone.game.features.sign_up_api.SignUpNavigationApi
import com.brokentelephone.game.features.theme.ThemeScreen
import com.brokentelephone.game.features.user_details.UserDetailsScreen
import com.brokentelephone.game.features.user_friends.UserFriendsScreen
import com.brokentelephone.game.features.welcome_api.WelcomeNavigationApi
import com.brokentelephone.game.features.welcome_api.WelcomeRoute
import com.brokentelephone.game.forgot_password_api.ForgotPasswordNavigationApi
import com.brokentelephone.game.nav_api.KEY_FORCE_REFRESH
import com.brokentelephone.game.nav_api.NavigationRoute
import com.brokentelephone.game.nav_api.safePopBackStack
import com.brokentelephone.game.navigation.routes.Routes
import com.brokentelephone.game.post_details_api.PostDetailsNavigationApi
import com.brokentelephone.game.profile_api.ProfileNavigationApi
import com.brokentelephone.game.sign_in_api.SignInNavigationApi
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {

        navigation<AuthGraph>(startDestination = WelcomeRoute) {
            welcomeNavigationApi.screen(navController, this)
            signUpNavigationApi.screen(navController, this)
            signInNavigationApi.screen(navController, this)
            forgotPasswordNavigationApi.screen(navController, this)
            chooseAvatarNavigationApi.screen(navController, this)
            chooseUsernameNavigationApi.screen(navController, this)
        }

        navigation<MainGraph>(startDestination = DashboardRoute) {

            dashboardNavigationApi.screen(navController, this)
            profileNavigationApi.screen(navController, this)
            postDetailsNavigationApi.screen(navController, this)
            drawNavigationApi.screen(navController, this)

            composable<Routes.DescribeDrawing>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.DescribeDrawing>()
                DescribeDrawingScreen(
                    sessionId = route.sessionId,
                    onBackClick = navController::safePopBackStack,
                    onPostSubmitted = {
                        navController.getBackStackEntry(Routes.Dashboard)
                            .savedStateHandle[KEY_FORCE_REFRESH] = true

                        navController.popBackStack(Routes.Dashboard, inclusive = false)
                    }
                )
            }

            composable<Routes.ChainDetails>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
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
            ) { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.ChainDetails>()
                val viewModel: ChainDetailsViewModel =
                    koinViewModel { parametersOf(route.postId, route.userId) }
                ChainDetailsScreen(
                    viewModel = viewModel,
                    onBackClick = navController::safePopBackStack,
                    onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                    },
                )
            }

            composable<Routes.EditProfile>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    val route = targetState.destination.route
                    if (route?.contains("EditUsername") == true || route?.contains("EditAvatar") == true || route?.contains(
                            "EditEmail"
                        ) == true || route?.contains("EditBio") == true
                    ) {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    } else {
                        ExitTransition.None
                    }
                },
                popEnterTransition = {
                    val route = initialState.destination.route
                    if (route?.contains("EditUsername") == true || route?.contains("EditAvatar") == true || route?.contains(
                            "EditEmail"
                        ) == true || route?.contains("EditBio") == true
                    ) {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    } else {
                        EnterTransition.None
                    }
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                EditProfileScreen(
                    onBackClick = navController::safePopBackStack,
                    onEditPhotoClick = {
//                        navController.navigateSingle(Routes.EditAvatar)
                    },
                    onEditUsernameClick = {
//                        navController.navigateSingle(Routes.EditUsername)
                    },
                    onEditBioClick = {
//                        navController.navigateSingle(Routes.EditBio)
                    },
                    onEditEmailClick = {
//                        navController.navigateSingle(Routes.EditEmail)
                    },
                )
            }

            composable<Routes.EditUsername>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                EditUsernameScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.EditBio>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                EditBioScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.EditAvatar>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                EditAvatarScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.Settings>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    val route = targetState.destination.route
                    if (route?.contains("AccountSettings") == true || route?.contains("NotificationSettings") == true || route?.contains(
                            "Language"
                        ) == true || route?.contains("Theme") == true || route?.contains("BlockedUsers") == true
                    ) {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    } else {
                        ExitTransition.None
                    }
                },
                popEnterTransition = {
                    val route = initialState.destination.route
                    if (route?.contains("AccountSettings") == true || route?.contains("NotificationSettings") == true || route?.contains(
                            "Language"
                        ) == true || route?.contains("Theme") == true || route?.contains("BlockedUsers") == true
                    ) {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    } else {
                        EnterTransition.None
                    }
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                SettingsScreen(
                    onBackClick = navController::safePopBackStack,
                    onNavigateToWelcome = {
                        navController.navigate(Routes.Welcome) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onAccountSettingsClick = {
//                        navController.navigateSingle(Routes.AccountSettings)
                    },
                    onNotificationsClick = {
//                        navController.navigateSingle(Routes.NotificationSettings)
                    },
                    onLanguageClick = {
//                        navController.navigateSingle(Routes.Language)
                    },
                    onThemeClick = {
//                        navController.navigateSingle(Routes.Theme)
                    },
                    onBlockedUsersClick = {
//                        navController.navigateSingle(Routes.BlockedUsers)
                    },
                    onNavigateToDraw = { route ->
//                        navController.navigateSingle(route)
                        onBannerDismissed()
                    },
                    onNavigateToDescribeDrawing = { route ->
//                        navController.navigateSingle(route)
                        onBannerDismissed()
                    },
                )
            }

            composable<Routes.AccountSettings>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    val route = targetState.destination.route
                    if (route?.contains("BlockedUsers") == true) {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    } else {
                        ExitTransition.None
                    }
                },
                popEnterTransition = {
                    val route = initialState.destination.route
                    if (route?.contains("BlockedUsers") == true) {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    } else {
                        EnterTransition.None
                    }
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                AccountSettingsScreen(
                    onBackClick = navController::safePopBackStack,
                    onNavigateToWelcome = {
                        navController.navigate(Routes.Welcome) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable<Routes.EditEmail>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                EditEmailScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.BlockedUsers>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                BlockedUsersScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.Language>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                LanguageScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.Notifications>(
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(300)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                NotificationsScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.NotificationSettings>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                NotificationSettingsScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }

            composable<Routes.Theme>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                ThemeScreen(
                    onBackClick = navController::safePopBackStack,
                )
            }


            composable<Routes.UserDetails>(
                enterTransition = {
                    val from = initialState.destination.route
                    if (from?.contains("Dashboard") == true || from?.contains("PostDetails") == true || from?.contains(
                            "ChainDetails"
                        ) == true || from?.contains("Friends") == true || from?.contains("AddFriend") == true
                    ) {
                        slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300)
                        )
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    }
                },
                exitTransition = {
                    val to = targetState.destination.route
                    if (to?.contains("UserFriends") == true) {
                        ExitTransition.None
                    } else {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    }
                },
                popEnterTransition = {
                    val from = initialState.destination.route
                    if (from?.contains("UserFriends") == true) {
                        EnterTransition.None
                    } else {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    }
                },
                popExitTransition = {
                    val to = targetState.destination.route
                    if (to?.contains("Dashboard") == true || to?.contains("PostDetails") == true || to?.contains(
                            "ChainDetails"
                        ) == true || to?.contains("Friends") == true || to?.contains("AddFriend") == true
                    ) {
                        slideOutVertically(
                            targetOffsetY = { it },
                            animationSpec = tween(300)
                        ) + fadeOut(animationSpec = tween(200))
                    } else {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    }
                }
            ) { backStackEntry ->
                val route = backStackEntry.toRoute<Routes.UserDetails>()
                UserDetailsScreen(
                    userId = route.userId,
                    onBackClick = navController::safePopBackStack,
                    onFriendsClick = { userId ->
//                        navController.navigateSingle(Routes.UserFriends(userId = userId))
                    },
                    onPostClick = { postId, userId ->
//                        navController.navigateSingle(Routes.ChainDetails(postId = postId, userId = userId))
                    },
                    onNavigateBackWithForceUpdate = {
                        val isDashboardInBackStack = navController.previousBackStackEntry
                            ?.destination?.route?.contains("Dashboard") == true

                        if (isDashboardInBackStack) {
                            navController.getBackStackEntry(Routes.Dashboard)
                                .savedStateHandle[KEY_FORCE_REFRESH] = true
                        }
                        navController.safePopBackStack()
                    },
                )
            }

            composable<Routes.Friends>(
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                },
                exitTransition = {
                    if (targetState.destination.route?.contains("AddFriend") == true) {
                        slideOutHorizontally(
                            targetOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeOut(animationSpec = tween(200))
                    } else {
                        ExitTransition.None
                    }
                },
                popEnterTransition = {
                    if (initialState.destination.route?.contains("AddFriend") == true) {
                        slideInHorizontally(
                            initialOffsetX = { -it / 3 },
                            animationSpec = tween(250)
                        ) + fadeIn(animationSpec = tween(200))
                    } else {
                        EnterTransition.None
                    }
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                }
            ) {
                FriendsScreen(
                    onBackClick = navController::safePopBackStack,
                    onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                    },
                    onAddFriendClick = {
//                        navController.navigateSingle(Routes.AddFriend)
                    },
                )
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
                    onBackClick = {},
//                    onBackClick = navController::safePopBackStack,
                    onUserClick = { userId ->
//                        navController.navigateSingle(Routes.UserDetails(userId = userId))
                    },
                )
            }

            composable<Routes.CreatePost>(
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = tween(300)
                    )
                },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = {
                    slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = tween(300)
                    ) + fadeOut(
                        animationSpec = tween(200)
                    )
                }
            ) {
                CreatePostScreen(
//                    onBackClick = navController::safePopBackStack,
//                    onPostCreated = navController::safePopBackStack
                )
            }
        }
    }
}

// 1111