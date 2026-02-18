package com.broken.telephone.features.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.broken.telephone.features.dashboard.content.DashboardContent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = koinViewModel(),
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    DashboardContent(
        state = state,
        modifier = modifier
    )
}
