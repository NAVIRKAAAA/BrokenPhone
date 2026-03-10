package com.brokentelephone.game.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.brokentelephone.game.core.locale.LocalizedContextWrapper
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme
import com.brokentelephone.game.core.theme.LocalAppLanguage
import com.brokentelephone.game.domain.settings.AppTheme
import com.brokentelephone.game.domain.settings.Language
import com.brokentelephone.game.features.bottom_nav_bar.AppNavBottomBar
import com.brokentelephone.game.navigation.nav_graph.AppNavGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private var keepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(1000)
            keepSplashScreen = false
        }

        enableEdgeToEdge()

        mainViewModel.initializeDefaultLanguage(Locale.getDefault().language)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val state by mainViewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()

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

            val locale = remember(state.language) {
                when (state.language) {
                    Language.ENGLISH -> Locale.ENGLISH
                    Language.UKRAINIAN -> Locale.forLanguageTag("uk")
                }
            }
            val localizedContext = remember(locale) {
                LocalizedContextWrapper(base = this@MainActivity, locale = locale)
            }

            CompositionLocalProvider(
                LocalAppLanguage provides state.language,
                LocalContext provides localizedContext,
            ) {
                BrokenTelephoneTheme(
                    darkTheme = isDarkTheme
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {

                        AppNavGraph(navController = navController)

                        AppNavBottomBar(
                            navController = navController,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .navigationBarsPadding()
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(0.85f)
                        )
                    }
                }
            }
        }

    }
}