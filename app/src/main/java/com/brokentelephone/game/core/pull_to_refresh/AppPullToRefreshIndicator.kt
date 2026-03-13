package com.brokentelephone.game.core.pull_to_refresh

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.brokentelephone.game.core.theme.BrokenTelephoneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPullToRefreshIndicator(
    state: PullToRefreshState,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier,
) {
    PullToRefreshDefaults.Indicator(
        state = state,
        isRefreshing = isRefreshing,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier,
    )
}

@Preview
@Composable
fun AppPullToRefreshIndicatorPreview() {
    BrokenTelephoneTheme(
        darkTheme = false
    ) {
        AppPullToRefreshIndicator(
            state = rememberPullToRefreshState(),
            isRefreshing = true,
        )
    }

}
