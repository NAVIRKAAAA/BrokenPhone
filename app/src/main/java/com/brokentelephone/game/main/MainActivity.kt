package com.brokentelephone.game.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.brokentelephone.game.core.R
import com.brokentelephone.game.core.banner.ActiveSessionBanner
import com.brokentelephone.game.core.dialog.ConfirmDialog
import com.brokentelephone.game.core.dialog.LoadingDialog
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.LocalAppLanguage
import com.brokentelephone.game.domain.model.settings.AppTheme
import com.brokentelephone.game.features.bottom_nav_bar.AppNavBottomBar
import com.brokentelephone.game.navigation.nav_graph.AppNavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashscreen.setKeepOnScreenCondition { !mainViewModel.state.value.isReady }

        enableEdgeToEdge()

        intent.data?.let { mainViewModel.handleAuthLink(it) }

        WindowCompat.setDecorFitsSystemWindows(window, false)

//        lifecycleScope.launch(Dispatchers.IO) {
//            try {
//                FirestoreTestDataSeeder(FirebaseFirestore.getInstance()).seedNotifications()
//            } catch (e: Exception) {
//                Log.d("LOG_TAG", "E: $e")
//            }
//        }

        setContent {
            val state by mainViewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            LaunchedEffect(Unit) {
                mainViewModel.sideEffects.collectLatest { effect ->
                    when (effect) {
                        is MainSideEffect.NavigateToDraw -> {
//                            navController.navigateSingle(effect.route)
                        }

                        is MainSideEffect.NavigateToDescribeDrawing -> {
//                            navController.navigateSingle(effect.route)
                        }

                        is MainSideEffect.NavigateToSignIn -> {
//                            navController.navigate(Routes.Welcome) {
//                                popUpTo(0) { inclusive = true }
//                            }
//
//                            navController.navigate(Routes.SignIn(email = effect.email))
                        }
                    }
                }
            }

            val isDarkTheme = when (state.theme) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }

            LaunchedEffect(isDarkTheme) {
                val controller = WindowCompat.getInsetsController(window, window.decorView)
                controller.isAppearanceLightStatusBars = !isDarkTheme
                controller.isAppearanceLightNavigationBars = false
            }

            CompositionLocalProvider(
                LocalAppLanguage provides state.language
            ) {
                BrokenTelephoneTheme(darkTheme = isDarkTheme) {

                    state.startDestination?.let { startDestination ->
                        LaunchedEffect(state.pendingRoutes) {
                            if (state.pendingRoutes.isNotEmpty()) {
                                state.pendingRoutes.forEach { route -> navController.navigate(route) }
                                delay(350)
                                mainViewModel.onPendingRoutesConsumed()
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            AppNavGraph(
                                startDestination = startDestination,
                                navController = navController,
                                onBannerDismissed = mainViewModel::onBannerDismissed,
                            )

                            AppNavBottomBar(
                                navController = navController,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .navigationBarsPadding()
                                    .padding(bottom = 16.dp)
                                    .fillMaxWidth(0.85f)
                            )

                            ActiveSessionBanner(
                                visible = state.isBannerVisible,
                                formattedTime = state.bannerFormattedTime,
                                progress = state.bannerProgress,
                                isLoading = state.isBannerLoading,
                                onContinueClick = mainViewModel::onBannerContinueClick,
                                onDismiss = mainViewModel::onBannerDismissed,
                                modifier = Modifier.align(Alignment.TopCenter),
                            )
                        }
                    }

                    if (state.isLoading) {
                        LoadingDialog()
                    }

                    state.sessionDataError?.let { message ->
                        ConfirmDialog(
                            title = stringResource(R.string.error_session_data_title),
                            body = message,
                            confirmText = stringResource(R.string.error_session_data_retry),
                            cancelText = stringResource(R.string.common_cancel),
                            onDismiss = mainViewModel::onSessionErrorDismissed,
                            onConfirm = mainViewModel::initializeSession,
                            confirmButtonColor = MaterialTheme.colorScheme.primary,
                            isLoading = state.isSessionLoading,
                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.data?.let { mainViewModel.handleAuthLink(it) }
    }
}