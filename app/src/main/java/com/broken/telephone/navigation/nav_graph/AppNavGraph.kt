package com.broken.telephone.navigation.nav_graph

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
import androidx.navigation.toRoute
import com.broken.telephone.features.chain_details.ChainDetailsScreen
import com.broken.telephone.features.chain_details.ChainDetailsViewModel
import com.broken.telephone.features.create_post.CreatePostScreen
import com.broken.telephone.features.dashboard.DashboardScreen
import com.broken.telephone.features.describe_drawing.DescribeDrawingScreen
import com.broken.telephone.features.draw.DrawScreen
import com.broken.telephone.features.post_details.PostDetailsScreen
import com.broken.telephone.features.post_details.PostDetailsViewModel
import com.broken.telephone.features.welcome.WelcomeScreen
import com.broken.telephone.navigation.routes.Routes
import com.broken.telephone.navigation.utils.navigateSingle
import com.broken.telephone.navigation.utils.safePopBackStack
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Welcome,
        modifier = modifier,
    ) {
        composable<Routes.Welcome>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            WelcomeScreen(
                onContinueAsGuest = {
                    navController.navigateSingle(Routes.Dashboard) {
                        popUpTo(Routes.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<Routes.Dashboard>(
            enterTransition = { EnterTransition.None },
            exitTransition = {
                if (targetState.destination.route?.contains("PostDetails") == true) {
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(250)
                    ) + fadeOut(animationSpec = tween(200))
                } else {
                    ExitTransition.None
                }
            },
            popEnterTransition = {
                if (initialState.destination.route?.contains("PostDetails") == true) {
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
            DashboardScreen(
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
                onDrawContinue = { postId ->
                    navController.navigateSingle(Routes.Draw(postId = postId))
                },
                onDescribeDrawingContinue = { postId ->
                    navController.navigateSingle(Routes.DescribeDrawing(postId = postId))
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
                postId = route.postId,
                onBackClick = navController::safePopBackStack,
                onPostSubmitted = {
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
                postId = route.postId,
                onBackClick = navController::safePopBackStack,
                onPostSubmitted = {
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