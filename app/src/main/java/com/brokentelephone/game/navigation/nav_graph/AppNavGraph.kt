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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.brokentelephone.game.features.account_settings.AccountSettingsScreen
import com.brokentelephone.game.features.blocked_users.BlockedUsersScreen
import com.brokentelephone.game.features.chain_details.ChainDetailsScreen
import com.brokentelephone.game.features.chain_details.ChainDetailsViewModel
import com.brokentelephone.game.features.choose_avatar.ChooseAvatarScreen
import com.brokentelephone.game.features.choose_username.ChooseUsernameScreen
import com.brokentelephone.game.features.create_post.CreatePostScreen
import com.brokentelephone.game.features.dashboard.DashboardScreen
import com.brokentelephone.game.features.dashboard.DashboardViewModel
import com.brokentelephone.game.features.describe_drawing.DescribeDrawingScreen
import com.brokentelephone.game.features.draw.DrawScreen
import com.brokentelephone.game.features.edit_avatar.EditAvatarScreen
import com.brokentelephone.game.features.edit_profile.EditProfileScreen
import com.brokentelephone.game.features.edit_username.EditUsernameScreen
import com.brokentelephone.game.features.language.LanguageScreen
import com.brokentelephone.game.features.notifications.NotificationsScreen
import com.brokentelephone.game.features.post_details.PostDetailsScreen
import com.brokentelephone.game.features.post_details.PostDetailsViewModel
import com.brokentelephone.game.features.profile.ProfileScreen
import com.brokentelephone.game.features.settings.SettingsScreen
import com.brokentelephone.game.features.sign_in.SignInScreen
import com.brokentelephone.game.features.sign_up.SignUpScreen
import com.brokentelephone.game.features.theme.ThemeScreen
import com.brokentelephone.game.features.welcome.WelcomeScreen
import com.brokentelephone.game.navigation.routes.Routes
import com.brokentelephone.game.navigation.utils.navigateSingle
import com.brokentelephone.game.navigation.utils.safePopBackStack
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val KEY_FORCE_REFRESH = "force_refresh"

@Composable
fun AppNavGraph(
    startDestination: Routes,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<Routes.Welcome>(
            enterTransition = { EnterTransition.None },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("SignUp") == true || route?.contains("SignIn") == true) {
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
                if (route?.contains("SignUp") == true || route?.contains("SignIn") == true) {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = { ExitTransition.None }
        ) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigateSingle(Routes.SignUp)
                },
                onSignIn = {
                    navController.navigateSingle(Routes.SignIn)
                },
                onNavigateToDashboard = {
                    navController.navigateSingle(Routes.Dashboard) {
                        popUpTo(Routes.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.Dashboard>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {

            val viewModel: DashboardViewModel = koinViewModel()
            val savedStateHandle = it.savedStateHandle

            LaunchedEffect(Unit) {
                val isForceRefresh = savedStateHandle.remove<Boolean>(KEY_FORCE_REFRESH) == true

                if(isForceRefresh) {
                    viewModel.onRefresh()
                }
            }

            DashboardScreen(
                viewModel = viewModel,
                onPostClick = { postId ->
                    navController.navigateSingle(Routes.PostDetails(postId = postId))
                }
            )
        }

        composable<Routes.PostDetails>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("Draw") == true || route?.contains("ChainDetails") == true) {
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
                if (route?.contains("Draw") == true || route?.contains("ChainDetails") == true) {
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
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.PostDetails>()
            val viewModel: PostDetailsViewModel = koinViewModel { parametersOf(route.postId) }
            PostDetailsScreen(
                viewModel = viewModel,
                onBackClick = navController::safePopBackStack,
                navigateBackWithForceUpdate = {
                    navController.getBackStackEntry(Routes.Dashboard)
                        .savedStateHandle[KEY_FORCE_REFRESH] = true

                    navController.safePopBackStack()
                },
                onDrawContinue = { sessionId ->
                    navController.navigateSingle(Routes.Draw(sessionId = sessionId))
                },
                onDescribeDrawingContinue = { sessionId ->
                    navController.navigateSingle(Routes.DescribeDrawing(sessionId = sessionId))
                },
                onViewHistoryClick = { postId ->
                    navController.navigateSingle(Routes.ChainDetails(postId = postId))
                },
            )
        }

        composable<Routes.Draw>(
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
            val route = backStackEntry.toRoute<Routes.Draw>()
            DrawScreen(
                sessionId = route.sessionId,
                onBackClick = navController::safePopBackStack,
                onPostSubmitted = {
                    navController.getBackStackEntry(Routes.Dashboard)
                        .savedStateHandle[KEY_FORCE_REFRESH] = true

                    navController.popBackStack(Routes.Dashboard, inclusive = false)
                }
            )
        }

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
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            }
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.ChainDetails>()
            val viewModel: ChainDetailsViewModel = koinViewModel { parametersOf(route.postId) }
            ChainDetailsScreen(
                viewModel = viewModel,
                onBackClick = navController::safePopBackStack,
            )
        }

        composable<Routes.SignIn>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                if (targetState.destination.route?.contains("SignUp") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("SignUp") == true) {
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
            SignInScreen(
                onBackClick = navController::safePopBackStack,
                onSignedIn = {
                    navController.navigateSingle(Routes.Dashboard) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigateSingle(Routes.SignUp)
                },
            )
        }

        composable<Routes.SignUp>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                if (targetState.destination.route?.contains("SignIn") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("SignIn") == true) {
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
            SignUpScreen(
                onBackClick = navController::safePopBackStack,
                onSignedUp = {
                    navController.navigateSingle(Routes.ChooseAvatar) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.navigateSingle(Routes.SignIn)
                },
            )
        }

        composable<Routes.Profile>(
            enterTransition = { EnterTransition.None },
            exitTransition = {
                val route = targetState.destination.route
                if (route?.contains("PostDetails") == true || route?.contains("EditProfile") == true || route?.contains(
                        "Settings"
                    ) == true
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
                if (route?.contains("PostDetails") == true || route?.contains("EditProfile") == true || route?.contains(
                        "Settings"
                    ) == true
                ) {
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeIn(animationSpec = tween(200))
                } else {
                    EnterTransition.None
                }
            },
            popExitTransition = { ExitTransition.None }
        ) {
            ProfileScreen(
                onPostClick = { postId  ->
                    navController.navigateSingle(Routes.ChainDetails(postId = postId))
                },
                onSignInClick = {
                    navController.navigateSingle(Routes.SignIn)
                },
                onGetStartedClick = {
                    navController.navigateSingle(Routes.SignUp)
                },
                onEditClick = {
                    navController.navigateSingle(Routes.EditProfile)
                },
                onSettingsClick = {
                    navController.navigateSingle(Routes.Settings)
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
                if (route?.contains("EditUsername") == true || route?.contains("EditAvatar") == true) {
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
                if (route?.contains("EditUsername") == true || route?.contains("EditAvatar") == true) {
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
                    navController.navigateSingle(Routes.EditAvatar)
                },
                onEditUsernameClick = {
                    navController.navigateSingle(Routes.EditUsername)
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

        composable<Routes.ChooseAvatar>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = tween(250)
                ) + fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = tween(250)
                ) + fadeIn(animationSpec = tween(200))
            },
        ) {
            ChooseAvatarScreen(
                navigateToChooseUsername = {
                    navController.navigateSingle(Routes.ChooseUsername)
                },
            )
        }

        composable<Routes.ChooseUsername>(
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
            },
        ) {
            ChooseUsernameScreen(
                onBackClick = navController::safePopBackStack,
                navigateToFeed = {
                    navController.navigateSingle(Routes.Dashboard) {
                        popUpTo(0) { inclusive = true }
                    }
                },
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
                if (route?.contains("AccountSettings") == true || route?.contains("Notifications") == true || route?.contains(
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
                if (route?.contains("AccountSettings") == true || route?.contains("Notifications") == true || route?.contains(
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
                    navController.navigateSingle(Routes.AccountSettings)
                },
                onNotificationsClick = {
                    navController.navigateSingle(Routes.Notifications)
                },
                onLanguageClick = {
                    navController.navigateSingle(Routes.Language)
                },
                onThemeClick = {
                    navController.navigateSingle(Routes.Theme)
                },
                onBlockedUsersClick = {
                    navController.navigateSingle(Routes.BlockedUsers)
                },
                onNavigateToDraw = { route ->
                    navController.navigateSingle(route)
                },
                onNavigateToDescribeDrawing = { route ->
                    navController.navigateSingle(route)
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
                if (targetState.destination.route?.contains("BlockedUsers") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("BlockedUsers") == true) {
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
            NotificationsScreen(
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
                onBackClick = navController::safePopBackStack,
                onPostCreated = navController::safePopBackStack
            )
        }
    }
}