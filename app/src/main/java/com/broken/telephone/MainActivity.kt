package com.broken.telephone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.broken.telephone.core.theme.BrokenTelephoneTheme
import com.broken.telephone.features.bottom_nav_bar.AppNavBottomBar
import com.broken.telephone.navigation.nav_graph.AppNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController = rememberNavController()

            BrokenTelephoneTheme(
                darkTheme = false
            ) {

                Box(
                    modifier = Modifier.fillMaxSize()
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