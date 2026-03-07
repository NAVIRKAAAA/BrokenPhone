package com.broken.telephone.main

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.core.theme.LocalAppLanguage
import com.broken.telephone.domain.settings.AppTheme
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBar
import com.broken.telephone.navigation.nav_graph.AppNavGraph
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

            CompositionLocalProvider(LocalAppLanguage provides state.language) {
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